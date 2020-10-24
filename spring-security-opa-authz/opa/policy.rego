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

allow {
  some username, i
  input.method == "GET"
  input.path = ["salary", username]
  data.users[i].name == input.name
  data.users[i].subordinates[_] == username
}
