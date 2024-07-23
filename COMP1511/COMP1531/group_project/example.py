
def channel_details_v1(auth_user_id, channel_id):
    store = data_store.get()

    all_members = []
    owner_members = []

    switch = False

    for channel in store['channels']:
        if channel['channel_id'] == channel_id:
            switch = True

            if not auth_user_id in channel['all_members']:
                raise AccessError("Authorised user is not a member of the channel")

            name = channel['name']
            is_public = channel['is_public']

            owner_id = channel['owner_members'][0]
            for user in store['users']:
                if owner_id == user['u_id']:
                    owner_members.append(
                        {
                            'u_id': user['u_id'],
                            'email': user['email'],
                            'name_first': user['name_first'],
                            'name_last': user['name_last'],
                            'handle_str': user['handle'],
                        }
                    )
            for member_id in channel['all_members']:
                for user in store['users']:
                    if member_id == user['u_id']:
                        all_members.append(
                            {
                                'u_id': user['u_id'],
                                'email': user['email'],
                                'name_first': user['name_first'],
                                'name_last': user['name_last'],
                                'handle_str': user['handle'],
                            }
                        )

    if switch == False:
        raise InputError("Invalid channel ID")    

    return {
        'name': name,
        'is_public': is_public,
        'owner_members': owner_members,
        'all_members': all_members,
    }