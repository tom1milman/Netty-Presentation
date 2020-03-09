# Netty Chat Server Demo
First demo for Netty presentation for distributed systems course.

## Running Demo
Make sure that the Maven builds without failing:
```
mvn clean
mvn install
```
### Starting Server
`<port>` the port you want the server to run on. 

<details>
 <summary>Windows:</summary>
 
```
StartServer.bat <port>
```

</details>

<details>
 <summary>Linux:</summary>
 
```
StartServer <port>
```

</details>

### Starting Client
`<server ip>` *optional* - server IP. by default it's `localhost`.

`<port>` - Server port.

<details>
<summary>Windows:</summary>
 
```
StartClient.bat <server ip> <port>
```

</details>

<details>
<summary>Linux:</summary>
 
```
StartClient <server ip> <port>
``` 

 </details>

