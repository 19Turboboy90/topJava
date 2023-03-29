### Get All Meals
> `curl` http://localhost:8080/topjava/rest/meals 

-----
### Get Meal by ID
> `curl` http://localhost:8080/topjava/rest/meals/100003

-----
### Delete Meal by ID
> `curl` http://localhost:8080/topjava/rest/meals/100013

-----
### Create Meal
> `curl` http://localhost:8080/topjava/rest/meals 

{"dateTime": "2023-03-28T17:00","description": "яблочко","calories": 100}

-----
### Update Meal
> `curl` http://localhost:8080/topjava/rest/meals/100003

{"dateTime": "2020-01-30T10:00:00","description": "Завтрак","calories": 1500}

-----
### Filtered Meal by starting date
> `curl` http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-31

-----
### Filtered Meals by all parameters
> `curl` http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-31&startTime=10:00&endDate=2020-01-31&endTime=20:00