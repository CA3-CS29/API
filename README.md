# CS29 CA3 API

This repository contains files for the CA3 client-side application's API. The API is automatically deployed to <https://ca3-api.herokuapp.com/>. Keep in mind that the API uses a free tier of Heroku Dynos, so the first requests may have to cold boot and will take a bit longer to boot.

## Tests

To run tests:

```shell
./gradlew test build
```

All new features should be covered by a reasonable number of unit tests.

## Contributing

Clone the repo:

```shell
git@stgit.dcs.gla.ac.uk:tp3-2020-CS29/api.git
```

This may require the VPN.

To contribute to the repo create a feature branch based on the feature that you are implementing using:

```shell
git checkout -b feature/#issuenumber-example-feature
```

After pushing to the repo, open a merge request and make sure the pipeline passes. All merge requests are subject to code review.

## Resources

Some resources on the stack used to get started with developing for this repo:

* [Spring Boot](https://spring.io/projects/spring-boot)
* [Lombok](https://projectlombok.org/)

## Contact

You can contact us at cs29tp@gmail.com.
