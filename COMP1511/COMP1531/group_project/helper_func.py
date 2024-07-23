
def request_delete():
    return requests.delete(config.url + 'clear/v1')

def login_post(list_param):
    data = dict(zip(['email', 'password'], list_param))
    return requests.post(config.url + 'auth/login/v2', json=data)

def register_post(list_param):
    data = dict(zip(['email', 'password', 'name_first', 'name_last'], list_param))
    return requests.post(config.url + 'auth/register/v2', json=data)

def chnls_create_post(list_param):
    data = dict(zip(['token', 'name', 'is_public'], list_param))
    return requests.post(config.url + 'channels/create/v2', json = data)

def chnl_join_post(list_param):
    data = dict(zip(['token', 'channel_id'], list_param))
    return requests.post(request_channel_join_post, json=data)

def chnl_invite_post(list_param):
    data = dict(zip(input_channel_invite, list_param))
    return requests.post(request_channel_invite_post, json=data)



import pytest
import requests
import json
from src import config

@pytest.fixture
def request_delete():
    return requests.delete(config.url + 'clear/v1')

def register_post(list_param):
    data = dict(zip(['email', 'password', 'name_first', 'name_last'], list_param))
    return requests.post(config.url + 'auth/register/v2', json=data)

def login_post(list_param):
    data = dict(zip(['email', 'password'], list_param))
    return requests.post(config.url + 'auth/login/v2', json=data)


def test_auth_login_v2_invalid_email_does_not_belong_to_user(request_delete):
    request_delete
    register_post(['z5359933@ad.unsw.edu.au', 'password', 'Holly', 'Chen'])
    register_post(['z5359934@ad.unsw.edu.au', 'passwords', 'Holly', 'Chen'])
    resp = login_post(['z5359932@ad.unsw.edu.au', 'password'])
    assert resp.status_code == 400

def test_auth_login_v2_invalid_email_not_registered(request_delete):  
    request_delete
    resp = login_post(['z5359932@ad.unsw.edu.au', 'password'])
    assert resp.status_code == 400

def test_auth_login_v2_incorrect_password_1(request_delete):
    request_delete
    register_post(['z5359932@ad.unsw.edu.au', 'password', 'Holly', 'Chen'])
    resp = login_post(['z5359932@ad.unsw.edu.au', 'passwo'])
    assert resp.status_code == 400

def test_auth_login_v2_incorrect_password_2(request_delete):
    request_delete
    register_post(['z5359657@ad.unsw.edu.au', 'passwordbyholly', 'Holly', 'Chen'])
    resp = login_post(['z5359657@ad.unsw.edu.au', 'byholly'])
    assert resp.status_code == 400

def test_auth_login_v2_correct_password(request_delete):
    request_delete
    resp1 = register_post(['z5359932@ad.unsw.edu.au', 'password', 'Holly', 'Chen'])
    resp2 = login_post(['z5359932@ad.unsw.edu.au', 'password'])
    assert resp2.json()['auth_user_id'] == resp1.json()['auth_user_id']

def test_auth_login_v2_auth_user_id(request_delete):
    request_delete
    register_post(['z5359932@ad.unsw.edu.au', 'password', 'Holly', 'Chen'])
    resp = login_post(['z5359932@ad.unsw.edu.au', 'password'])
    assert isinstance(resp.json()['auth_user_id'], int) 

def test_auth_login_v2_token(request_delete):
    request_delete
    register_post(['z5359932@ad.unsw.edu.au', 'password', 'Holly', 'Chen'])
    resp = login_post(['z5359932@ad.unsw.edu.au', 'password'])
    assert isinstance(resp.json()['token'], str) 

def test_auth_register_v2_invalid_email_1(request_delete):
    request_delete
    resp = register_post(['email', 'password', 'Holly', 'Chen'])
    assert resp.status_code == 400

def test_auth_register_v2_invalid_email_2(request_delete):
    request_delete
    resp = register_post(['aaa','password', 'Holly', 'Chen'])
    assert resp.status_code == 400

def test_auth_register_v2_invalid_email_3(request_delete):
    request_delete
    resp = register_post(['@email.com', 'password', 'Chen', 'Hu'])
    assert resp.status_code == 400

def test_auth_register_v2_invalid_email_4(request_delete):
    request_delete
    resp = register_post(['Janet@', 'pssword', 'Wen', 'Pen'])
    assert resp.status_code == 400

def test_auth_register_v2_no_duplicates(request_delete):
    request_delete
    register_post(['z5359932@ad.unsw.edu.au', 'password', 'Holly', 'Chen'])
    resp = register_post(['z5359932@ad.unsw.edu.au', 'password', 'Holly', 'Chen'])
    assert resp.status_code == 400

# Password can not have less than 6 characters
def test_auth_register_v2_invalid_password_4_characters(request_delete):
    request_delete
    register = register_post(['z5359932@ad.unsw.edu.au', 'word', 'Holly', 'Chen'])
    assert register.status_code == 400

def test_auth_register_v2_invalid_password_5_characters(request_delete):
    request_delete
    resp = register_post(['z5359932@ad.unsw.edu.au', 'words', 'Holly', 'Chen'])
    assert resp.status_code == 400

# name_first cannot have less than 1 and more than 50 characters
def test_auth_register_v2_invalid_firstname_0_characters(request_delete):
    request_delete
    resp = register_post(['z5359932@ad.unsw.edu.au', 'password', '', 'Chen'])
    assert resp.status_code == 400

def test_auth_register_v2_invalid_firstname_51_characters(request_delete):
    request_delete
    resp = register_post(['z5359932@ad.unsw.edu.au', 'password', 
    'HollyPeiQiHollyPeiQiHollyPeiQiHollyPeiQiHollyPeiQii', 'Chen'])
    assert resp.status_code == 400

def test_auth_register_v2_invalid_firstname_52_characters(request_delete):
    request_delete
    resp = register_post(['z5359932@ad.unsw.edu.au', 'password', 
    'HollyPeiQiHollyPeiQiHollyPeiQiHollyPeiQiHollyPeiQiii', 'Chen'])
    assert resp.status_code == 400

# name_last cannot have less than 1 and more than 50 characters
def test_auth_register_v2_invalid_lastname_0_characters(request_delete):
    request_delete
    resp = register_post(['z5359932@ad.unsw.edu.au', 'password', 'Holly', ''])
    assert resp.status_code == 400

def test_auth_register_v2_invalid_lastname_51_characters(request_delete):
    request_delete
    resp = register_post(['z5359932@ad.unsw.edu.au', 'password', 'Holly', 
        'ChenChenChenChenChenChenChenChenChenChenChenChenChe'])
    assert resp.status_code == 400
    
def test_auth_register_v2_invalid_lastname_52_characters(request_delete):
    request_delete
    resp = register_post(['z5359932@ad.unsw.edu.au', 'password', 'Holly', 
        'ChenChenChenChenChenChenChenChenChenChenChenChenChe'])
    assert resp.status_code == 400

def test_auth_register_v2_auth_user_id(request_delete):
    request_delete
    resp = register_post(['z5359932@ad.unsw.edu.au', 'password', 'Holly', 'Chen'])
    assert isinstance(resp.json()['auth_user_id'], int)

def test_auth_register_v2_token(request_delete):
    request_delete
    resp = register_post(['z5359932@ad.unsw.edu.au', 'password', 'Holly', 'Chen'])
    assert isinstance(resp.json()['token'], str)
