#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Wed May  4 15:59:05 2022

@author: ramizouari
"""

from mongoengine import Document
from mongoengine import *

class Store(Document):
    name=StringField(min_length=2,max_length=64)
    description=StringField(max_length=256)
    
    
class Product(Document):
    name=StringField(min_length=2,max_length=64)
    price=FloatField(min_value=0)


class Sale(Document):
    storeId=ObjectIdField()
    productId=ObjectIdField()
    quantity=IntField(min_value=0)
    date=DateTimeField()
    price=FloatField(min_value=0) # added
    productname=StringField(max_length=64) 
    storename=StringField(max_length=64) 


    def __repr__(self):
        return f"{self.productId},{self.productname},{self.price},{self.storeId},{self.storename},{self.quantity},{self.date}"
    
    def __str__(self):
        return self.__repr__()