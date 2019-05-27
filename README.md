# OpenMRS module ERP
OpenMRS module to expose an API endpoint to access ERP data

### Supported ERPs and models:
- Odoo 10
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
