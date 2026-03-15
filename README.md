<p align="center">
  <img src="Logo.svg" alt="Project Logo" width="500"/>
</p>

# CodeX Server Load Tests

This repository contains the code for the CodeX server load tests.

## Project Structure

- `start.py`: Initialization script
- `build.sbt`: SBT configuration
- `project/`: SBG Plugin configuration
- `src/test`: Source files. Main index and script file
    - `scala/com/load`: Source files
        - `simulations`: Single simulation classes
    - `resources`: Data needed for the tests
        - `bodies`: Request body configurations
        - `data`: Miscellaneous request data
            - `app/environment`: Infrastructure to get environment configuration
            - `app/model/network`: Connection to the REST-API of the backend
            - `app/model/domain`: Domain models for the entities
            - `app/model/authentication`: Abstraction over the authentication. Keeps the currently authenticated account

## Running
To run the test, execute the `start.py` script:

```bash
python3 start.py
```

For the tests to work, the `EMAIL_VERIFICATION_REQUIRED` environment variable for the server must be set to `false`.

## Environment parameters

The following environment parameters are mandatory.

| Parameter Name            | Description                                                                                                      |
|---------------------------|------------------------------------------------------------------------------------------------------------------|
| CODEX_LOAD_ADMIN_EMAIL    | The admin-email address.                                                                                         |
| CODEX_LOAD_ADMIN_PASSWORD | The admin-email password.                                                                                        |
| CODEX_LOAD_LLM_BASE       | The base URL of the OpenAI-API to use for conversations. Should in most cases be a URL to a mock-implementation. |
| CODEX_LOAD_BASE           | The base URL of the server to test.                                                                              |
