package authz

default allow = false

allow {
  some username
  input.method == "GET"
  input.path = ["salary", username]
  input.name == username
}

allow {
  input.method == "GET"
  input.path = ["salary", _]
  input.authorities[_] == "ROLE_HR"
}
