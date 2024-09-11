
# Search Autocomplete Service

A basic Search Autocomplete Service which has takes inputs search words and create a dataset on the basis of that dataset.
It has a O(1) time complexity for finding the top K searched words for a given search query. And this behaviour remains intact even after insertion of millions of words.

## API endpoints
1. `/search`
   This endpoint makes an HTTP GET request to localhost:8080/search in order to retrieve top k searched words in the completer dataset
#### Request Body
This request does not require a request body.

#### Response Body
- `status` *(string)*: Indicates the status of the response.
- `error` *(boolean)*: Specifies whether an error occurred during the search.
- `message` *(string)*: Provides additional information about the search response.
- `topKSearchedWords` *(array of strings)*: Contains the top searched words.

#### Example JSON
```
{
    "status": "",
    "error": true,
    "message": "",
    "topKSearchedWords": [""]
}
```
2. `/search/{prefix}`
   This endpoint makes an HTTP GET request to localhost:8080/search/{prefix} in order to retrieve top k searched words starting the provided prefix
#### Request Body
This request does not require a request body.

#### Response Body
- `status` *(string)*: Indicates the status of the response.
- `error` *(boolean)*: Specifies whether an error occurred during the search.
- `message` *(string)*: Provides additional information about the search response.
- `topKSearchedWords` *(array of strings)*: Contains the top searched words.

#### Example JSON
```
{
    "status": "",
    "error": true,
    "message": "",
    "topKSearchedWords": [""]
}
```
3. `/search/newWords`
   This endpoint makes an HTTP POST request to localhost:8080/search/newWords in order to
   add new searched words to the dataset along with their frequency
#### Request Body
This request contians a list of json object with below properties

`word`  (string, required): The word to be searched.
`frequency` (integer, required): The frequency of the word.

#### Response
`status` *(string)*: The status of the response.
`error` *(boolean)*: Indicates if an error occurred.
`message` *(string)*: Additional information or error message.
`topKSearchedWords` *(array)*: List of top searched words.

#### Example Request Body:

JSON
```
[
  {
    "word": "word1",
    "frequency": 2
  },
  {
    "word": "word2",
    "frequency": 9
  }
]
```

#### Example Response:

JSON

```
{
  "status": null,
  "error": false,
  "message": "words successfully inserted into tri",
  "topKSearchedWords": null
}
```

## Configuration
Create an S3 bucket from the AWS console and create a new user and attach to it the policy for accessing the S3 bucket
Add the application.properties file in the resources directory and add the required credentials, bucket name and region needed to access the S3 bucket create

#### Example
```
server.shutdown.grace-period=60s
accessKey=iam-user-access-key
secret=iam-user-secrer-access-key
bucketName=s3-bucket-name
region=bucket-region
```

## Running the application
Run the `SearchAutoCompleteService.main` method to start the server and accepting the requests
