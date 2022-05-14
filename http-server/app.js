const express = require('express')
const { exec } = require('child_process');
const app = express()
const port = 3000

app.get('/train/:url', (req, res) => {
    exec('bash sparkimport '+req.params["url"], (err, stdout, stderr) => {
        if (err) {
            //some err occurred
            console.error(err)
        } else {
            // the *entire* stdout and stderr (buffered)
            console.log(`${stdout}`);
            // console.log(`stderr: ${stderr}`);
        }
    })
  res.send('Train Job in progress')
})

app.get('/predict/:filepath', (req, res) => {
    exec('bash sparkpredict /root/http-server/forecast.model /spark/' +req.params["filepath"], (err, stdout, stderr) => {
        if (err) {
            //some err occurred
            console.error(err)
        } else {
            // the *entire* stdout and stderr (buffered)
            console.log(`${stdout}`);
            // console.log(`stderr: ${stderr}`);
        }
    })
    res.send('Predict Job in progress')
})
app.listen(port, () => {
  console.log(`Example app listening on port ${port}`)
})
