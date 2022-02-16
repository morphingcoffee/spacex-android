# spacex-wip

### Decisions

#### Nullability of fields in Data Transfer Objects (DTOs)

Based on the provided
services [SpaceX-API discussion](https://github.com/r-spacex/SpaceX-API/issues/290#issuecomment-582650941)
, most of the fields can be null. So, in the design the DTO parameters are mostly nullable, and
naturally, that's propagated throughout the layers of the client app.

### v4 vs v5 API endpoints

Although v5 is available for rocket launches, v4 was used for all remote service requests. That's
mainly to reduce the variance in case one of them experience problems and the other does not.