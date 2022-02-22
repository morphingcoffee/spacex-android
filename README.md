# spacex-wip

### Sketch

[Open in draw.io](https://drive.google.com/file/d/1VyL3SQWdsXMWnkCpl57tjIKqt_8wvbEK/view?usp=sharing)

### Decisions

#### `/launches/query` endpoint

The regular all launches endpoint includes only an id reference to the rocket, since it's an object
from another collection, so it is not enough to retrieve the required rocket `name` and `type`
fields.

Instead of doing a second request to look up rocket info to `/rockets`, I've consulted
with [the wiki](https://github.com/r-spacex/SpaceX-API/blob/master/docs/queries.md) to switch
to `/launches/query` instead & get the required rocket info as part of the single request.

#### Nullability of fields in Data Transfer Objects (DTOs)

Based on the provided
services [SpaceX-API discussion](https://github.com/r-spacex/SpaceX-API/issues/290#issuecomment-582650941)
, most of the fields can be null. So, in the design the DTO parameters are mostly nullable, and
naturally, that's propagated throughout the layers of the client app.

### v4 vs v5 API endpoints

I was considering to use the same version for all of the endpoints, but switched to the
available `v5` for launches, since the data is not coupled to the company info `v4` endpoint.

### Testing

Make sure you run instrumented tests with the device unlocked, otherwise you may face the following
error: ```java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState```.

