# OpenMRS module ERP
OpenMRS module to expose an API endpoint to access ERP data

### Supported ERPs and models:
- Odoo 14
  + Orders
  + Invoices
  + Maintenance Requests (Requires Maintenance app, tested with app version 14.0.1.0)
  + Stock Inventory Adjustments (Requires Inventory app, tested with app version 14.0.1.1)
- Odoo 10 (_deprectated since 2.x_)
  + Orders
  + Invoices

### Required setup

##### Properties file:

Create the `erp.properties` file in the application directory (usually `~/.OpenMRS/`) and add the ERP connection details into it, as per the example below:
```
erp.host=localhost
erp.port=8069
erp.user=admin
erp.password=password
erp.database=odoo
```
The contents may vary with the ERP you are connecting to.
You can find the properties details in the specific implementation file of [ErpConnection.java](./api/src/main/java/org/openmrs/module/erp/api/ErpConnection.java) for your ERP [here](./api/src/main/java/org/openmrs/module/erp/api/impl/).

##### OpenMRS privilege:

Ensure the OpenMRS user has the `Get ERP objects` OpenMRS privilege assigned.

### How To Query For Maintenance Requests And Stock Inventory Adjustments
Currently, the only supported operation for the maintenance requests and stock inventory adjustments resources is to 
fetch all active ones.

Note that only active maintenance requests are returned i.e. the ones in a stage that is not terminal, these are the 
stages that are not marked as done.

Also note that only in progress stock inventory adjustments are returned, drafts and completed ones are excluded.

For maintenance requests and stock inventory, by design the module currently does not include each and every odoo model 
field on its associated entity classes, the property names don't necessary match the model field names
in odoo, this is because odoo uses snakecase field naming while the OpenMRS module being a Java project uses camelcase,
therefore an odoo model field like **schedule_date** would be property **scheduleDate** in the returned payload.

Property mappings between `MaintenanceRequest` entity class in the module and odoo `maintenance.request` model

| Module Property | Odoo Field | Module Type |
| ----------- | ----------- | --------------- |
| id | id | Integer |
| name | name | String |
| requestDate | request_date | Date |
| scheduleDate | schedule_date | Date |
| duration | duration | Double |
| equipment | equipment_id | Equipment |


Property mappings between `Equipment` entity class in the module and odoo `maintenance.equipment` model

| Module Property | Odoo Field | Module Type |
| ----------- | ----------- | --------------- |
| id | id | Integer |
| name | name | String |
| serialNo | serial_no | String |
| location | location | String |
| categoryId | category_id | Integer |
| categoryName | equipment category name | String |


Property mappings between `InventoryAdjustment` entity class in the module and odoo `stock.inventory`

| Module Property | Odoo Field | Module Type |
| ----------- | ----------- | --------------- |
| id | id | Integer |
| name | name | String |
| date | date | Date |


Below are the http request details to fetch maintenance requests

**Method:** `GET`

**URL:** `/openmrs/ws/rest/v1/erp/maintenancerequest`

**URL Parameters:**

_Optional:_ `v`, the object representation, see below.

Below are the http request details to fetch stock inventory adjustments

**Method:** `GET`

**URL:** `/openmrs/ws/rest/v1/erp/inventoryadjustment`

**URL Parameters:**

_Optional:_ `v`, the object representation, see below.

The v parameter allows a client to specify how the returned object should be modeled/represented, there is 4 types of 
representations available:
- `default` Returns a subset set of entity properties in as defined on the mapped entity class in the module, usually 
  includes the **id**, **name**, **display** and some other extra useful properties.
- `full` Similar to the default representation but returns all the properties defined on the mapped entity class in this 
  module.
- `custom` Allows a client to pass the mapped entity property names you want to retrieve. E.g. for maintenance requests, provide it with 
  the following example syntax:
```
?v=custom:name,equipment,requestDate,scheduleDate
```
- `erp` Allows a client to request the entity in the same representation as the ERP web service would return, this can 
  be useful when a client needs to fetch specific custom fields that were added to a model.
```
?v=erp
```
If you wish to get specific fields as they represented by the ERP web service, E.g. for maintenance requests, provide it 
with the following example syntax:
```
?v=erp:name,equipment_id,request_date,schedule_date
```

**NOTE**: Datetime fields for `ref`, `default`, `full` and `custom` representations are formatted just like any other 
date field in the web service module i.e. include timezone information but for `erp` representation the datetime fields 
are returned as they are in odoo i.e. in UTC and with no extra timezone information. 


### How to query the ERP objects?

_Example with ERP Orders_

Log in OpenMRS.

### Fetch a single order by ID:
**Method:** `GET`

**URL:** `/openmrs/ws/rest/v1/erp/order/{id}`
- `{id}`, a single order ID

**URL Parameters:**

_Optional:_
- `rep`, the object representation, see below.

**Success Response:**

_Code:_ 200
_Response:_ A single record in the type of `Map<String, Object>`. The exact fields returned depend on the ERP and representation used.
```
{
  "invoice_ids": null,
  "pricelist_id": 1,
  "origin": null,
  ...
}
```

### Fetch orders via filters:

**Method:** `POST`

**URL:** `/openmrs/ws/rest/v1/erp/order`

**URL Parameters:**

_Optional:_
- `rep`, the object representation, see below.

**Data Parameters:**

_Required:_
- filters: an array of maps with 3 keys: `field`, `comparison`, `value`
For instance:
```
{
  "filters": [
    {
      "field": "amount_total",
      "comparison": "<",
      "value": "1000"
    },
    {
      "field": "create_date",
      "comparison": ">",
      "value": "2019-05-19"
    }
  ]
}
```

**Success Response:**

_Code:_ 200

_Response:_ A list of records in the type of `List<Map<String, Object>>`. The exact fields returned depend on the ERP and representation used.
```
[
  {
    "invoice_ids": null,
    "pricelist_id": 1,
    "origin": null
  },
  {
    "invoice_ids": null,
    "pricelist_id": 1,
    "origin": null
  },
  {
    "invoice_ids": null,
    "pricelist_id": 1,
    "origin": null
  }
]
```

### More about the `rep` optional URL parameter:

**URL Parameter:**

_Optional:_ `rep`, the object representation

One can specify how the returned object should be modeled/represented.
There is 3 types of representations available:
- `default`
- `full`
- `custom`

The `default` object representation is documented in the specific implementation of [ErpOrderService.java](./api/src/main/java/org/openmrs/module/erp/api/ErpOrderService.java) for the ERP you are using.

The `full` representation simply returns all fields returned by the ERP.

The `custom` representation allows you to pass the fields you want to retrieve. Provide it with the following example syntax:
```
?rep=custom:amount_total,create_date,id
```

### Get in touch
Find us on [OpenMRS Talk](https://talk.openmrs.org/): sign up, start a conversation and ping us with the mentions starting with @mks... in your message.

-------

This module development has been sponsored by **Hôpital Sacré Coeur** in Haiti:

<img src="readme/crudem-hsc-logo.png" alt="HSC logo" width="300"/>
