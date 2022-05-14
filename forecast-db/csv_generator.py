"""
Created on Wed May  4 16:12:11 2022

@author: ramizouari
"""

from dao import Store, Product, Sale
import mongoengine
import numpy as np
import pandas as pd
from datetime import datetime, timedelta

connection_params={   
 "port":27017,
 "db":"forecast-test",
 "host":"localhost"
 }


connection=mongoengine.connect(**connection_params)

def generate_manual_data(DocumentClass,data,drop_collection=True):
    if drop_collection:
        DocumentClass.drop_collection()
    for datum in data:
        datum.save()
def indexes(DocumentClass):
    ids=[]
    for O in DocumentClass.objects:
        ids.append(O.id)
    return ids

def indexes_from_list(list):
    return [o.id for o in list]

#List of stores
stores=[
        Store(name="Aziza",description="""A very bad store.
0 out of 10"""),
        Store(name="MG",description="""An average store.
A very bad manager."""),
        Store(name="Giant",description="""An excelllent store.
Very good service.""")
        ]
              

#List of products
products=[
    Product(name="Legion 5",price=3500),
    Product(name="Air Conditionner",price=1500),
    Product(name="Radiator",price=2000)
    ]


# generate_manual_data(Store,stores)
# generate_manual_data(Product,products)


"""
@Model: The sales S is a random function which is a function of the month 0 <= m < 12, product p, and store s. 
Furthermore, S(m,p,s)= L(s) × B(m,p).
Where: 
    - B(m,p) is a truncated normal random variable with mean µ,δ that are deterministic functions of the product. representing
     the normal sale of the product itself, independent of the store.
    - L(s) = G(s)+1  with G(s) a poisson random variable  representing the magnitude of the a sale for that store
@Observation: An observation O is a uniform random variable in the product space Pr × St × Dt
Where: 
    - Pr is the set of products
    - St is the set of stores
    - Dt is a subset of dates, which is a discrete range with a day as a step
"""
product_statistics_per_day=[ [(100,15),(200,30),(300,20),(400,50),(500,50),(1000,100),(1500,500),(1500,700),(1000,300),(400,50),(200,50),(100,25)] ,
                            [(50,15),(40,7),(100,20),(200,50),(500,50),(2000,300),(2000,100),(1500,100),(1000,400),(200,50),(150,50),(50,25)],
                            [(3000,200),(3000,200),(2000,700),(1500,500),(1000,100),(200,50),(200,20),(100,10),(1000,200),(1500,500),(2000,200),(3000,100)]
                            ]
store_goodness=[1,2,5]


seed=0
n_observation=20000
np.random.seed(seed=seed)


products = Product.objects
# product_ids = indexes(Product)
product_ids = indexes_from_list(products)
n_products=len(product_ids)

# store_ids=indexes(Store)
stores = Store.objects
store_ids= indexes_from_list(stores)
n_stores=len(store_ids)

observed_stores=np.random.choice(store_ids,n_observation)
observed_products=np.random.choice(product_ids,n_observation)

dates_space=np.arange(datetime(2018,1,1), datetime(2022,4,1), timedelta(days=1)).astype(datetime)
observed_dates=np.random.choice(dates_space,n_observation)

observations=[]
bad_examples=[]
for storeId,productId,date in zip(observed_stores,observed_products,observed_dates):
    k=product_ids.index(productId)
    r=store_ids.index(storeId)
    params=product_statistics_per_day[k][observed_dates[0].month-1]
    goodness=store_goodness[r]
    quantity=  (np.random.poisson(goodness)+1)*max(np.random.normal(*params),0)
    observations.append(Sale(storeId=storeId,productId=productId,date=date,quantity=quantity,price=products[k].price, storename=stores[r].name, productname=products[k].name))

with open("dataset.csv", 'wb') as f:
        f.writelines( [ f"{o}\n".encode("utf-8") for o in observations])


# generate_manual_data(Sale,observations,drop_collection=True)

#for observation in observations:
#    try:
#        observation.save()
#    except mongoengine.ValidationError:
#        print(observation.quantity)