u_id = 1
users = [{
        "u_id": 1, 
        "email": "Helo2@ad.unsw.edu.au", 
        "password": "8bb0cf6eb9b17d0f7d22b456f121257dc1254e1f01665370476383ea776df414", 
        "name_first": "hollaaaaaaaaaaaa", 
        "name_last": "Huuuuuuu", 
        "handle": "hollaaaaaaaaaaaahuuu", 
        "token": ["eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoxLCJzZXNzaW9uX2lkIjoyOTQzfQ.ua68pEe-PH4BH6YNDc_jSBahxSt4GH-IZIHMVElOzGk"], 
        "session_list": [2943], 
        "permission_id": 1,
}]

for user in users:
    if user['u_id'] == u_id:
        user['name_first'] = 'Removed'
        user['name_last'] = 'user'
        keys_to_change = ['permission_id', 'email', 'token', 'session_list', 'handle', 'password']
        for key in keys_to_change:
            user[key] = None
print(users)