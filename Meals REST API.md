## **Get all meals**

Returns list of all meals.

- **URL**

  /rest/meals

- **Method:**

  `GET`

- **URL Params**

  none

- **Data Params**

  None

- **Success Response:**

  - **Code:** 200 OK



## Get filtered meals

 Returns list of meals filtered by date and time 

- **URL**

  /rest/meals/filter

- **Method:**

  `GET`

- **URL Params**

  **Optional:**

  `startDate=[2020-12-31]`

  `startTime=[23:59:59]`

  `endDate=[2020-12-31]`

  `endTime=[23:59:59]`

- **Data Params**

  None

- **Success Response:**

  - **Code:** 200 OK



## **Get meal**

Returns meal with id.

- **URL**

  /rest/meals/{id}

- **Method:**

  `GET`

- **URL Params**

  id=[integer]

- **Data Params**

  None

- **Success Response:**

  - **Code:** 200 OK



## **Delete meal**

Delete meal with id.

- **URL**

  /rest/meals/{id}

- **Method:**

  `DELETE`

- **URL Params**

  id=[integer]

- **Data Params**

  None

- **Success Response:**

  - **Code:** 204 No content



## **Create meal**

Creates new meal.

- **URL**

  /rest/meals

- **Method:**

  `POST`

- **URL Params**

  None

- **Data Params**

  {

  "dateTime":"2015-06-01T21:00:00",
  "description":"new meal",
  "calories":"300"
  }

- **Success Response:**

  - **Code:** 201 Created



## Update meal

Update meal with id

- **URL**

  /rest/meals/{id}

- **Method:**

  `POST`

- **URL Params**

  None

- **Data Params**

  {

  "dateTime":"2015-06-01T22:00:00",
  "description":"updated meal",
  "calories":"300"
  }

- **Success Response:**

  - **Code:** 204 No content