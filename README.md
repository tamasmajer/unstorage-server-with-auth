# Implementing an Unstorage server with authentication

[Unstorage](https://github.com/unjs/unstorage) is a simple key-value store interface, with some default drivers,
including an http driver.
However, authentication is not supported. This project is about how we could simply build authentication support into an
Unstorage server.

This server simply implements two Unstorage compatible interfaces: the Auth API and the Data API.
Both needs an access token in the header.
The original http driver does not support adding a header, so we can use
this [modified http driver](https://www.npmjs.com/package/unstorage-driver-http-headers).

The Auth API is a user to access token map, a simple key-value store.
It can be called after a successful external authentication, for example from a simple NextAuth site.
To access the Auth API we need to use a fix admin access token, defined by an environment variable (it is for now in the
application.properties).

After we have saved an access token for the authenticated user, we can use that token to call the Data API using a new
Unstorage client.

Why Java and MySQL? I just wanted to brush up on my Java skills. A node version would also be very easy to do.

## Status

The Unstorage interface is implemented, however still need to implement:

- data storage limit per request
- data storage limit per user
- data transfer limit per user
- request limit per user
- concurrent request limit
- remove old records after fix time
- remove old records if storage space is scarce

## How to try

Start this java server locally and connect to it from node.
First add a user/token pair using the Auth API using an Unstorage client,
then create an other Unstorage client with the user's token.
Then call it to store/access user data in the Data API.

### How to download and start the java server

```shell
git clone git@github.com:tamasmajer/unstorage-server-with-auth.git
cd unstorage-server-with-auth
./mvnw spring:boot run
```

You will need a MySQL server to store the data.
Set the connection parameters in the application.properties file.

### How to create a simple NodeJS client to test it

Create a project, add Untorage and the driver:

```shell
npm init -y
npm i unstorage unstorage-driver-http-headers
```

Create index.mjs:

```js
import { createStorage } from 'unstorage'
import createDriver from 'unstorage-driver-http-headers'

const getAuthApi = () => {
    const AUTH_URL = 'http://localhost:8081/api/auth'
    const AUTH_HEADERS = { 'Authorization': 'Bearer SECRET_ADMIN_KEY' }
    const driver = createDriver({ base: AUTH_URL, headers: AUTH_HEADERS })
    const auth = createStorage({ driver })
    return auth
}

const getDataApi = (token) => {
    const STORAGE_URL = 'http://localhost:8081/api/data'
    const STORAGE_HEADERS = { 'Authorization': 'Bearer ' + token }
    const driver = createDriver({ base: STORAGE_URL, headers: STORAGE_HEADERS })
    const storage = createStorage({ driver })
    return storage
}

const testServerWithAuth = async () => {
    const TOKEN = "token1"
    const USER = "user1"
    const authApi = getAuthApi()
    await authApi.setItem(USER, TOKEN)
    const storage = getDataApi(TOKEN)
    await storage.setItem("key1", "value1")
    const value1 = await storage.getItem("key1")
    const has1 = await storage.hasItem("key1")
    const keys1 = await storage.getKeys()
    await storage.removeItem("key1")
    const has2 = await storage.hasItem("key1")
    console.log({ value1, has1, keys1, has2 })
    // should print:
    // { value1: 'value1', has1: true, keys1: [ 'key1' ], has2: false }
}
testServerWithAuth()
```

Run:

```shell
node index.mjs
```
