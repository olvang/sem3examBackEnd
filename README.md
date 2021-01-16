[![Build Status](https://travis-ci.com/fdinsen/CA3.svg?branch=main)](https://travis-ci.com/fdinsen/CA3)

<p>All of this expects you have a local user for your MySql databse called dev with ax2 as password</p>

<p>Remember to start your local docker envoriment and run "docker-compose up -d" where your docker image is installed </p>

<p>Remember to give travis acces to your repository through <br></p>
https://travis-ci.com/account/repositories --> Mange repositories on github

<p>Remember to set the envorimental varibles in the branch in travis.</p>
<p>REMOTE_USER = [Your server user]</p>
<p>REMOTE_PW = [Your server password]</p>

<p>Change or add your own connection string in EMF-Creator at line 43 and 46</p>
<p>Add the connection string in docker-compose.yml on your digitalOcean under "enviroment"</p>

<p>Create database on digitalOcean to match your local setup</p>

<p>Rename the value in the <remote.server> tag to [Your server ip]/manager/text on line 19 </p>

<p>Change the value on line 24 in persistence.xml to match the local database on your machine</p>
