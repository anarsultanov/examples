package authz

default allow = false

allow {
  input.method == "GET"
  input.path = ["salary", _]
  input.authorities[_] == "ROLE_HR"
}

allow {
  some username
  input.method == "GET"
  input.path = ["salary", username]
  username == users_access[input.name][_]
}

users_graph[data.users[username].name] = edges {
  edges := data.users[username].subordinates
}

users_access[username] = access {
  users_graph[username]
  access := graph.reachable(users_graph, {username})
}
