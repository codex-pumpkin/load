import os

import requests

mail = os.getenv("CODEX_LOAD_ADMIN_EMAIL")
password = os.getenv("CODEX_LOAD_ADMIN_PASSWORD")
base_url = os.getenv("CODEX_LOAD_BASE")
llm_base_url = os.getenv("CODEX_LOAD_LLM_BASE")

# 1. Login request
login_response = requests.post(
    f"{base_url}/auth/login",
    json={
        "username": None,
        "email": mail,
        "password": password
    }
)

login_response.raise_for_status()
access_token = login_response.json()["access_token"]

# 2. Create LLM configuration
headers = {
    "Authorization": f"Bearer {access_token}",
    "Content-Type": "application/json"
}

llm_response = requests.post(
    f"{base_url}/llm-configurations",
    headers=headers,
    json={
        "name": "Mock",
        "model_name": "openai/gpt-5.4",
        "api_url": f"{llm_base_url}/v1",
        "api_key": ""
    }
)

llm_response.raise_for_status()
llm_id = llm_response.json()["id"]

os.system(f"sbt Gatling/test -DllmId={llm_id}")