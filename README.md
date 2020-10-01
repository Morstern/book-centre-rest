# book-centre-rest
RESTful service for bookstore (University project)
Used technologies, tools:
  - Spring boot,
  - Hibernate,
  - Lombok,
  - Swagger,
  - MySQL.

## Main goal
The main goal for this project was to create RESTful service which:
  - Uses more than 200 and 404 in result codes (I tried to used codes which are most accurate for the server action: 200, 201, 204, 404, 409, 412, 428, 500),
  - Uses header fields (If-Match and Location),
  - Uses valid HTTP method for server actions,
  - Uses valid URI addresses,
  - For creating new resources (client, purchase), server uses the POST Once Exactly concept (POST-PUT method, where POST creates empty object and server sends in the header localization of an object, and with PUT method server fills the data),
  - For updating resources, we check the If-Match value to solve lost-update problem (ETag value is created from hashcode on an object, if database object's fields have changed – the hashcode wouldn't match, so server signals that there's a new version of the resource).
 
 ## My personal goals
  - Test tool (Swagger) in order to write API documentation,
  - Test Hibernate mapper, which generates entities from database structure.
  

## API table for the overview (URI+HTTP Method → action)


| URI | GET | POST | PUT | PATCH | DELETE |
| - | - | - | - | - | - |
| /api/clients | amount of clients | create empty client | X | X | X |
| /api/clients/{id} | client info | X | update client | partial client update | delete client and all his purchases |
| /api/clients/{id}/purchases | get all client's purchases | X | X | X | X |
| /api/purchases | amount of purchases | create empty purchase | X | X | X |
| /api/purchases/{id} | get purchase info | X |  update purchase | X | delete unpaid purchase |
| /api/purchases/{id}/ispaid | check paid status | X | X | change paid status | X |
| /api/books | retrieve all books | X | X | X | X |
| /api/books/{id} | book info | X | update book | X | X |
| /api/books/{id}/amount | X | X | X | update amount | X |
| /api/purchases-merger | X | merge unpaid purchaseses | X | X | X |
