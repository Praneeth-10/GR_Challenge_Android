# GR_Challenge_Android
Preliminary Challenge

## Comments
- The Sample JSON object was missing a delimiter i.e. "," in the 2nd JSON Object.
- Please paste the JSON Data in Assets folder once project has been imported.
- The Project has been completed using Kotlin and Java.


## Assumptions
- The data should be entered properly in the JSON Format in the `events.json` file without missing out any delimiters. The file is located in assets folder in the Project.
- If there is `END` type data in JSON object, there should always be a `START` type JSON Object, as there would be no session with a START of the session for a particular `ID`. Even from the client application, the customer starts and his rental data is saved with `START` type, So, the JSON format is assumed to have no only `END` JSON Object of a ID.
- If there is only 1 JSON Object of an ID, then that should always be of type `START`.
- For the ID's where there is no `END` type, the values for "session end time" and "session duration" are given default values as "0", and same for the "boolean flag indicating if the car was returned later than" and "boolean flag indicating if the car was damaged" took default value as "false"
- There can only be maximum of 2 IDs that would be having same data, and strictly of each type(`START` and `END`). The ID's are to be UNIQUE. So, it will always be true from the client application side.
 

## Thinking.
- I like JSON objects and playing with them, life is much more easier to send the Data and recieve it whether be it any kind of huge data.
- First created 2 different Map<Int,JSONObject> to store the 2 different types, `START` and `END`.
- Once stored in different Maps I showed them via the recycler view on the first activity page.
- Now there is button to export the Data into SQLite Database. So, here first the Map with type `START` is insrted in a new table `car_Report` in a new Database `summary_Report`. the table has ID as the primary key.
- The SQLite table contains

```
id 
type
start_timestamp
end_timestamp
start_comments
end_comments
session_duration
session_expired
car_damaged
```

- By first adding the type `START`, the id's column being primary key and UNIQUE would be first completed by `START` Map.
- Then 2nd Map(`END`) would update the table by fetching the ID from it's own JSONObject(inside a map entry) and would update the all other remaining 5 columns from the fetched when searching for the ID.
- The creation and updation of table in the SQLite Database is done via coroutines for working on CPU IO thread rather than on main thread of applications for working on a Larger Data where it could take time to input the data and search the data.
- And once the Data is exported a new button is visible to go to the second activity where one can see the compiled Data with the required objectives mentioned in the challenge, populated onto a Recycler view.
