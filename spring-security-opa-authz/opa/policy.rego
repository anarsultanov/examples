package authz

default allow = false

allow {
  input.action == "read"
  input.resource.type == "salary"
  input.subject.authorities[_] == "ROLE_HR"
}

allow {
  input.action == "read"
  input.resource.type == "salary"
  input.resource.user == users_access[input.subject.name][_]
}

allow {
  input.action == "read"
  input.resource.type == "document"
  input.resource.owner == input.subject.name
}

users_graph[data.users[username].name] = edges {
  edges := data.users[username].subordinates
}

users_access[username] = access {
  users_graph[username]
  access := graph.reachable(users_graph, {username})
}
