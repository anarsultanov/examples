package authz

default allow = false

allow {
  some username
  input.method == "GET"
  input.path = ["salary", username]
  input.name == username
}
