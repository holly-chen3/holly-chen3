import { BACKEND_PORT } from './config.js';
// A helper you may want to use when uploading new images to the server.
import { fileToDataUrl, apiPost, apiAuthPost, apiAuthGet, apiAuthQueryGet, apiAuthPut, apiAuthDelete, appendAlert } from './helpers.js';

console.log('Let\'s go!');
// Global Variables
let globalToken = null;
let userID = null;
let channelList = [];
let channelID = null;
let channelMembers = [];
let messageStartCount = 0;
let notifications = [];

// Local Storage
const localStorageToken = localStorage.getItem('token');
const localStorageUserID = localStorage.getItem('userID');
if (localStorageToken !== null && localStorageUserID !== null) {
    globalToken = localStorageToken;
    userID = localStorageUserID;
}

// Back Button
document.querySelectorAll('.left-arrow').forEach((e) => {
    e.addEventListener('click', () => {
        showPage('channels');
    });
});
// ----------------------------------------------
//      Loading Public and Private Channels
// ----------------------------------------------
// Delete Channels
const deleteChannels = () => {
    document.querySelectorAll('.temporary').forEach(e => e.remove());
}
// For public and private channel page
const createChannel = (name, id) => {
    const channel = document.getElementById('channel-template').cloneNode(true);
    channel.removeAttribute('id');
    channel.classList.add('temporary');
    channel.classList.remove('hide');
    channel.querySelector('.channel-title').innerText = name;
    channel.querySelector('.channel-id').innerText = id;
    return channel;
}

const addPublicChannel = (name, id) => {
    const newChannel = createChannel(name, id);
    document
        .getElementById('public-channels')
        .appendChild(newChannel);
}
const addPrivateChannel = (name, id) => {
    const newChannel = createChannel(name, id);
    document
        .getElementById('private-channels')
        .appendChild(newChannel);
}
const publicPrivateChannel = (body, id) => {
    if (body.private === false) {
        addPublicChannel(body.name, id);
    } else {
        if (body.members.includes(Number(userID))) {
            addPrivateChannel(body.name, id);
        }
    }
}
const loadChannels = (body) => {
    channelList = body.channels;
    for (let i = 0; i < channelList.length; i++) {
        publicPrivateChannel(channelList[i], channelList[i].id);
    }
    updateChannels();
}
const channelLoad = () => {
    apiAuthGet('channel', globalToken, loadChannels);
}
// ----------------------------------------------
//                  Show Pages
// ----------------------------------------------
const showPage = (pageName) => {
    if (document.getElementById('channels').style.display === 'block') {
        deleteChannels();
    }
    document.getElementById('login').style.display = 'none';
    document.getElementById('registration').style.display = 'none';
    document.getElementById('channels').style.display = 'none';
    document.getElementById('channel-screen').style.display = 'none';
    document.getElementById('user-profile').style.display = 'none';
    document.getElementById('sender-user-profile').style.display = 'none';
    document.getElementById(`${pageName}`).style.display = 'block';
    if (pageName === 'login' || pageName === 'registration') {
        document.getElementById('nav').style.display = 'none';
        document.getElementById('header-logo').style.display = 'block';
    } else {
        document.getElementById('enable').style.display = Notification.permission === "granted" ? "none" : "block";
        document.getElementById('nav').style.display = 'block';
        document.getElementById('header-logo').style.display = 'none';
    }
    if (pageName === 'channels') {
        channelLoad();
    }
    if (pageName === 'channel-screen') {
        infiniteScroll();
    }
    if (pageName === 'user-profile') {
        loadUserProfile();
    }
}
// ----------------------------------------------
//           Bonus: Offcanvas Sidebar
// ----------------------------------------------
document.getElementById('channels-option').addEventListener('click', () => {
    showPage('channels');
});
document.getElementById('profile-option').addEventListener('click', () => {
    showPage('user-profile');
});
// ----------------------------------------------
//                   Login
// ----------------------------------------------

const inputData = (body) => {
    globalToken = body.token;
    localStorage.setItem('token', globalToken);
    userID = body.userId;
    localStorage.setItem('userID', userID);
    showPage('channels');
}
let loginForm = document.forms.login_form;
document.getElementById('submit-login').addEventListener('click', (event) => {
    event.preventDefault();
    const email = loginForm.elements.email1.value;
    const password = loginForm.elements.password1.value;
    apiPost('auth/login', { email, password }, inputData);
});

document.getElementById('register-now').addEventListener('click', (event) => {
    event.preventDefault();
    showPage('registration');
});

// ----------------------------------------------
//                   Logout
// ----------------------------------------------

const logout = () => {
    globalToken = null;
    userID = null;
    localStorage.clear();
    showPage('login');
}
document.getElementById('logout').addEventListener('click', (event) => {
    event.preventDefault();
    apiAuthPost('auth/logout', globalToken, {}, logout);
})

// ----------------------------------------------
//                  Register
// ----------------------------------------------

let registerForm = document.forms.register_form;
document.getElementById('submit-register').addEventListener('click', (event) => {
    event.preventDefault();
    const regEmail = registerForm.elements.email2.value;
    const regPassword = registerForm.elements.password2.value;
    const regConfirmPassword = registerForm.elements.confirm_password.value;
    const regName = registerForm.elements.name.value;
    if (regPassword !== regConfirmPassword) {
        appendAlert('Password and Confirm Password do not match');
        return;
    }
    if (regEmail === '' || regPassword === '' || regName === '') {
        appendAlert('Please input all fields');
    } else {
        const body = {
            email: regEmail,
            password: regPassword,
            name: regName
        }
        apiPost('auth/register', body, inputData);
    }
});

document.getElementById('back-to-login').addEventListener('click', () => {
    showPage('login');
});

// ----------------------------------------------
//            Create a New Channel
// ----------------------------------------------

let channelForm = document.forms.create_channel;
const saveChannel = document.getElementById('save-channel');
channelForm.elements.channel_name.addEventListener('keyup', () => {
    checkEnableSubmit(saveChannel, channelForm.elements.channel_name.value);
});

const checkEnableSubmit = (button, name) => {
    if (name === '') {
        button.disabled = true;
    } else {
        button.disabled = false;
    }
};
saveChannel.addEventListener('click', (event) => {
    event.preventDefault();
    newChannel();
    channelForm.reset();
});
const newChannelLoad = (body) => {
    publicPrivateChannel(body, channelID);
    updateChannels();
}
const addNewChannel = (body) => {
    channelID = body.channelId;
    apiAuthGet(`channel/${channelID}`, globalToken, newChannelLoad);
}
const newChannel = () => {
    const channelName = channelForm.elements.channel_name.value;
    let channelDescription = channelForm.elements.description.value;
    const channelPrivacy = channelForm.elements.privacy.value;
    if (channelDescription === '') {
        channelDescription = channelName + ' ' + channelPrivacy;
    }
    apiAuthPost('channel', globalToken, { 
        'name': channelName, 
        'private': channelPrivacy === 'private', 
        'description': channelDescription 
    }, addNewChannel);
}

const updateChannels = () => {
    document.querySelectorAll('.temporary').forEach(channel => {
        channel.addEventListener('click', () => {
            channelScreenLoad(channel.querySelector('.channel-id').innerText);
            showPage('channel-screen');
        });
    });
}
// ----------------------------------------------
//       Show Channel Details/Join Channel
// ----------------------------------------------

const showChannels = (show) => {
    const channelJoin = document.getElementById('channel-join');
    const channelDetails = document.getElementById('channel-homepage');
    if(show) {
        channelJoin.style.display = 'none';
        channelDetails.style.display = 'flex';
    } else {
        channelJoin.style.display = 'flex';
        channelDetails.style.display = 'none';
    }
};
const joinChannel = () => {
    showChannels(false);
}
const screenLoad = (body) => {
    deleteMessagesInChannel();
    messageLoad(channelID, 0);
    showChannels(true);
    const channelScreen = document.getElementById('channel-screen');
    channelScreen.querySelector('.individual-channel-name').innerText = body.name;
    channelScreen.querySelector('.individual-channel-description').innerText = body.description;
    channelScreen.querySelector('.privacy-setting').innerText = body.private ? 'Private': 'Public';
    let strDate = body.createdAt;
    channelScreen.querySelector('.creation-timestamp').innerText = strDate.substring(0, 10);
    const getName = (body) => {
        channelScreen.querySelector('.creator-name').innerText = body.name;
    }
    channelMembers = body.members;
    apiAuthGet(`user/${body.creator}`, globalToken, getName);
}
const channelScreenLoad = (_channelID) => {
    channelID = _channelID;
    apiAuthGet(`channel/${channelID}`, globalToken, screenLoad, joinChannel);
};

// ----------------------------------------------
//             Edit Channel Details
// ----------------------------------------------

const editChannelDetails = document.forms.edit_channel_details;
const channelScreen = document.getElementById('channel-screen');
const changeChannelDetails = document.getElementById('change-details');
const changeDetails = () => {
    apiAuthGet(`channel/${channelID}`, globalToken, screenLoad, joinChannel);
    editChannelDetails.reset();
} 
const editChannel = (channelID, channelName, channelDescription) => {
    const body = {
        name: channelName,
        description: channelDescription 
    }
    apiAuthPut(`channel/${channelID}`, globalToken, body, changeDetails);
};
changeChannelDetails.addEventListener('click', () => {
    let channelDescription = editChannelDetails.elements.change_channel_description.value;
    let channelName = editChannelDetails.elements.change_channel_name.value;
    if (channelDescription === '') {
        channelDescription = channelScreen.querySelector('.individual-channel-description').innerText;
    }
    if (channelName === '') {
        channelName = channelScreen.querySelector('.individual-channel-name').innerText;
    }
    editChannel(channelID, channelName, channelDescription);
});
editChannelDetails.elements.change_channel_name.addEventListener('keyup', () => {
    checkEnableSubmit(changeChannelDetails, editChannelDetails.elements.change_channel_name.value);
});
editChannelDetails.elements.change_channel_description.addEventListener('keyup', () => {
    checkEnableSubmit(changeChannelDetails, editChannelDetails.elements.change_channel_description.value);
})

// ----------------------------------------------
//       Let Users Join the Public Channels
// ----------------------------------------------

const joined = () => {
    channelScreenLoad(channelID);
}
document.getElementById('join-now').addEventListener('click', () => {
    apiAuthPost(`channel/${channelID}/join`, globalToken, {}, joined);
});

// ----------------------------------------------
//          Let Users Leave the Channels
// ----------------------------------------------

const left = () => {
    joinChannel();
}
document.getElementById('leave-channel').addEventListener('click', () => {
    apiAuthPost(`channel/${channelID}/leave`, globalToken, {}, left);
});
// MILESTONE 3: messages
// ----------------------------------------------
//            Viewing Channel Messages
// ----------------------------------------------
const createMessage = (body) => {
    const message = document.getElementById('message-template').cloneNode(true);
    message.removeAttribute('id');
    message.classList.add('temporary-message');
    message.classList.remove('hide');
    message.classList.add('message');
    const getSenderInfo = (data) => {
        message.querySelector('.sender-name').innerText = data.name;
        if (data.image) {
            message.querySelector('.sender-profile').src = data.image;
        }
    }
    apiAuthGet(`user/${body.sender}`, globalToken, getSenderInfo);
    let date = new Date(body.sentAt);
    message.querySelector('.message-timestamp').innerText = 'Sent At: ' + date.toISOString().substring(0, 10);
    if (body.message === undefined) {
        message.querySelector('.message-image').src = body.image;
        message.querySelector('.message-words').classList.add('hide');
        message.querySelector('.message-image').classList.add('active-image');
    } else {
        message.querySelector('.message-words').innerText = body.message;
        message.querySelector('.message-image').classList.add('hide');
    }
    message.querySelector('.message-words').innerText = body.message;
    message.querySelector('.pin-unpin').innerText = (body.pinned ? 'Unpin': 'Pin');
    message.querySelector('.edited-timestamp').innerText = (body.edited ? 'Edited At: ' + new Date(body.editedAt).toISOString().substring(0, 10) : '');
    message.querySelector('.delete-message').value = body.id;
    message.querySelector('.sender-id').value = body.sender;
    const userReact = body.reacts.find(e => e.user === Number(userID));
    if ((body.reacts.length === 0) && (userReact !== undefined)) {
        message.querySelector('.react-emoji').classList.remove('hide');
        message.querySelector('.react-emoji').innerText = userReact.react;
        hideReactions(message);
    }
    return message;
}
const messageCallback = (body) => {
    const messages = body.messages;
    Array.from(messages).forEach((message) => {
        document
        .querySelector('.message-container')
        .appendChild(createMessage(message));
    });
    updateMessages();
}
const messageLoad = (channelid, start) => {
    apiAuthQueryGet(`message/${channelid}`, `start=${start}`, globalToken, messageCallback);
};

const deleteMessagesInChannel = () => {
    document.querySelectorAll('.temporary-message').forEach(e => e.remove());
}
// ----------------------------------------------
//             Send Messages/Images
// ----------------------------------------------

const messageForm = document.forms.send_messages;
const loadMessage = () => {
    deleteMessagesInChannel();
    messageLoad(channelID, 0);
    const msg_container = document.getElementById('channel-screen');
    window.scrollTo(0, msg_container.scrollHeight);
}
const sendPhotoForm = document.forms.send_photo;
const uploadPhoto = sendPhotoForm.elements.upload_photo;
document.getElementById('save-photo').addEventListener('click', () => {
    if (uploadPhoto.files.length !== 0) {
        fileToDataUrl(uploadPhoto.files[0]).then((e) => {
            notifications.push({
                image: e,
                channelmembers: channelMembers.filter(e => e !== userID),
            });
            sendPhotoForm.reset();
            apiAuthPost(`message/${channelID}`, globalToken, { image: e }, loadMessage);
        });
    } else {
        appendAlert('No photo was uploaded');
    }
});
document.getElementById('send-message').addEventListener('click', (event) => {
    event.preventDefault();
    const newMessage = messageForm.elements.new_message.value;
    if (newMessage === '' || !/\S/.test(newMessage)) {
        appendAlert('Please input a valid message');
        return;
    }
    const body = { message: newMessage };
    messageForm.reset();
    notifications.push({
        message: newMessage,
        channelmembers: channelMembers.filter(e => e !== userID),
    });
    apiAuthPost(`message/${channelID}`, globalToken, body, loadMessage);
});
// ----------------------------------------------
//            Show Sender Profile
// ----------------------------------------------
const senderProfile = (body) => {
    if (body.image !== null) {
        document.getElementById('sender-user-picture').src = body.image;
    }
    document.getElementById('sender-user-name').innerText = body.name;
    document.getElementById('sender-user-bio').innerText = body.bio;
    document.getElementById('sender-user-email').innerText = body.email;
}
const loadSenderProfile = (element) => {
    element.querySelector('.sender-name').addEventListener('click', () => {
        showPage('sender-user-profile');
        const senderID = element.querySelector('.sender-id').value;
        apiAuthGet(`user/${senderID}`, globalToken, senderProfile);
    });
}
document.getElementById('back-to-messages').addEventListener('click', () => {
    showPage('channel-screen');
});
// ----------------------------------------------
//            Enlarge Image Messages
// ----------------------------------------------
const showImage = (image) => {
    const newImage = document.getElementById('photo-template').cloneNode(true);
    newImage.removeAttribute('id');
    newImage.classList.add('temporary-image');
    newImage.classList.add('carousel-item');
    newImage.classList.remove('hide');
    newImage.querySelector('.channel-photo').src = image.src;
    return newImage;
}
const deleteImages = () => {
    document.querySelectorAll('.temporary-image').forEach(e => e.remove());
}
const loadImages = (image) => {
    const messageImage = image.querySelector('.message-image');
    if (!messageImage.classList.contains('hide')) {
        const newImage = showImage(messageImage);
        document.querySelector('.carousel-inner').appendChild(newImage);
        messageImage.addEventListener('click', () => {
            newImage.classList.add('active');
            const pictureView = document.getElementById('picture-view');
            const modal = new bootstrap.Modal(pictureView);
            modal.show();
        });
    }
}
// ----------------------------------------------
//           Interaction with Messages
// ----------------------------------------------
const updateMessages = () => {
    deleteImages();
    document.querySelectorAll('.temporary-message').forEach(message => {
        deleteMessages(message);
        editMessages(message);
        react(message);
        unreact(message);
        if (message.querySelector('.pin-unpin').innerText === 'Unpin') {
            unpin(message);
        } else {
            pin(message);
        }
        loadSenderProfile(message);
        loadImages(message);
    });
};
// ----------------------------------------------
//                Delete Messages
// ----------------------------------------------
const deleteMessages = (message) => {
    message.querySelector('.delete-message').addEventListener('click', (event) => {
        event.preventDefault();
        apiAuthDelete(`message/${channelID}/${message.querySelector('.delete-message').value}`, globalToken, loadMessage);
    });
}
// ----------------------------------------------
//                 Edit Messages
// ----------------------------------------------
const editMessageForm = (message) => {
    message.querySelector('.edit').addEventListener('click', (event) => {
        event.preventDefault();
        const editInput = message.querySelector('.edit-input').value;
        const body = {message: editInput};
        apiAuthPut(`message/${channelID}/${message.querySelector('.delete-message').value}`, globalToken, body, loadMessage);
    });
    message.querySelector('.unedit').addEventListener('click', () => {
        message.querySelector('.form-edit').classList.add('hide');
        message.querySelector('.edit-message').classList.remove('unedit');
    });
}
const editMessages = (message) => {
    message.querySelector('.edit-message').addEventListener('click', (event) => {
        event.preventDefault();
        const editForm = message.querySelector('.form-edit');
        editForm.classList.remove('hide');
        message.querySelector('.edit-message').classList.add('unedit');
        editMessageForm(message);
    });
}
// ----------------------------------------------
//      Reacting and Unreacting to Messages
// ----------------------------------------------
const hideReactions = (reactions) => {
    reactions.querySelectorAll('.emoji').forEach(e => {
        e.classList.add('hide');
    });
}
const react = (reactions) => {
    reactions.querySelectorAll('.emoji').forEach(e => {
        e.addEventListener('click', () => {
            const body = { react: e.innerText }
            const reactCallback = () => {
                reactions.querySelector('.react-emoji').classList.remove('hide');
                reactions.querySelector('.react-emoji').innerText = e.innerText;
                hideReactions(reactions);
            }
            apiAuthPost(`message/react/${channelID}/${reactions.querySelector('.delete-message').value}`, globalToken, body, reactCallback);
        }, {once: true});
    });
}
const showReactions = (reactions) => {
    reactions.querySelectorAll('.emoji').forEach(e => {
        e.classList.remove('hide');
    });
}
const unreact = (reactions) => {
    reactions.querySelector('.react-emoji').addEventListener('click', () => {
        const body = { react: reactions.querySelector('.react-emoji').innerText };
        const unreactCallback = () => {
            reactions.querySelector('.react-emoji').classList.add('hide');
            showReactions(reactions);
        }
        apiAuthPost(`message/unreact/${channelID}/${reactions.querySelector('.delete-message').value}`, globalToken, body, unreactCallback);
    },  {once: true});
}

// ----------------------------------------------
//            Pin and Unpin Messages
// ----------------------------------------------
const anotherhandle = (element) => {
    const pinCallback = () => {
        element.querySelector('.pin-unpin').innerText = 'Unpin';
        unpin(element);
    }
    apiAuthPost(`message/pin/${channelID}/${element.querySelector('.delete-message').value}`, globalToken, {}, pinCallback);
}
const pin = (pin) => {
    pin.querySelector('.pin-unpin').addEventListener('click', () => {anotherhandle(pin)}, {once: true});
};
const handle = (element) => {
    const unpinCallback = () => {
        element.querySelector('.pin-unpin').innerText = 'Pin';
        pin(element);
    }
    apiAuthPost(`message/unpin/${channelID}/${element.querySelector('.delete-message').value}`, globalToken, {}, unpinCallback);
}
const unpin = (unpin) => {
    unpin.querySelector('.pin-unpin').addEventListener('click', () => {handle(unpin)}, {once: true});
}
// ----------------------------------------------
//            Get All Pinned Messages
// ----------------------------------------------
document.getElementById('pinned-messages').addEventListener('click', () => {
    const messages = document.getElementsByClassName('temporary-message');
    document.getElementById('pinned-messages').style.fontWeight = "500";
    document.getElementById('all-messages').style.fontWeight = "400";
    Array.from(messages).forEach((message) => {
        if(message.querySelector('.pin-unpin').innerText === 'Pin' && !message.classList.contains('hide')) {
            message.classList.remove('message');
            message.classList.add('hide');
        }
    });
});
// ----------------------------------------------
//               Get All Messages
// ----------------------------------------------
document.getElementById('all-messages').addEventListener('click', () => {
    const messages = document.getElementsByClassName('temporary-message');
    document.getElementById('all-messages').style.fontWeight = "500";
    document.getElementById('pinned-messages').style.fontWeight = "400";
    Array.from(messages).forEach((message) => {
        if(message.querySelector('.pin-unpin').innerText === 'Pin' && message.classList.contains('hide')) {
            message.classList.remove('hide');
            message.classList.add('message');
        }
    });
});

// Milestone 4
// ----------------------------------------------
//           Inviting Users to a Channel
// ----------------------------------------------
const deleteUsers = () => {
    document.querySelectorAll('.temporary-username').forEach(e => e.remove());
};
document.getElementById('invite-button').addEventListener('click', () => {
    Array.from(document.getElementsByClassName('user-checkbox')).forEach((e) => {
        if(e.checked) {
            channelMembers.push(Number(e.value));
            apiAuthPost(`channel/${channelID}/invite`, globalToken, { userId: Number(e.value) }, deleteUsers);
        }
    });
});
document.getElementById('close-button').addEventListener('click', () => {
    deleteUsers();
});
const checked = () => {
    const checkboxes = Array.from(document.getElementsByClassName('user-checkbox'));
    let checked = 0;
    checkboxes.forEach((e) => {
        e.addEventListener('change', () => {
            if(e.checked) {
                checked = checked + 1;
            } else {
                checked = checked - 1;
            }
            if(checked > 0) {
                document.getElementById('invite-button').disabled = false;
            } else {
                document.getElementById('invite-button').disabled = true;
            }
        });
    });
}
const createOneInvitee = (name, user) => {
    const invitee = document.getElementById('user-template').cloneNode(true);
    invitee.removeAttribute('id');
    invitee.classList.add('temporary-username');
    invitee.classList.remove('hide');
    invitee.classList.add('checkbox');
    invitee.querySelector('.user-name').innerText = name;
    invitee.querySelector('.user-checkbox').value = user;
    return invitee;
}
const showInvitees = (body) => {
    let users = body.users;
    users = users.filter(e => !channelMembers.includes(e.id));
    const names = [];
    Array.from(users).forEach((user) => {
        const addInvitee = (individual) => {
            names.push(individual.name);
            names.sort();
            if (names.length === users.length) {
                Array.from(names).forEach((name) => {
                    document.querySelector('.modal-body-username').appendChild(createOneInvitee(name, user.id));
                })
                checked();
            }
        }
        apiAuthGet(`user/${user.id}`, globalToken, addInvitee);
    });
}
document.getElementById('invite-users').addEventListener('click', () => {
    document.getElementById('invite-button').disabled = true;
    apiAuthGet('user', globalToken, showInvitees);
});
// ----------------------------------------------
//                User Profile
// ----------------------------------------------
const getUserDetails = (body) => {
    if (body.image !== null) {
        document.getElementById('image-user-profile').src = body.image;
        document.getElementById('edit-user-profile').src = body.image;
    }
    document.getElementById('profile-name').innerText = body.name;
    document.getElementById('profile-bio').innerText = body.bio;
    document.getElementById('profile-email').innerText = 'Email: ' + body.email;
}
const loadUserProfile = () => {
    apiAuthGet(`user/${userID}`, globalToken, getUserDetails);
}
// ----------------------------------------------
//               Change Password
// ----------------------------------------------
const newPasswordForm = document.forms.change_password;
const firstPassword = newPasswordForm.elements.user_password;
const secondPassword = newPasswordForm.elements.confirm_user_password;
const savePassword = () => {
    console.log();
}
document.getElementById('save-password').addEventListener('click', () => {
    apiAuthPut('user', globalToken, { password: newPasswordForm.elements.user_password.value }, savePassword);
});
const passwordMatch = () => {
    if(firstPassword.value === secondPassword.value) {
        document.getElementById('save-password').disabled = false;
    } else {
        document.getElementById('save-password').disabled = true;
    }
};
firstPassword.addEventListener('keyup', () => {
    passwordMatch();
});
secondPassword.addEventListener('keyup', () => {
    passwordMatch();
});
// toggle password between visible or hidden
document.getElementById('password-visible').addEventListener('click', () => {
    // if clicked, make password visible
    firstPassword.type = "text";
    secondPassword.type = "text";
    document.getElementById('password-visible').classList.add('hide');
    document.getElementById('password-invisible').classList.remove('hide');
});
document.getElementById('password-invisible').addEventListener('click', () => {
    // if clicked, make password invisible
    firstPassword.type = "password";
    secondPassword.type = "password";
    document.getElementById('password-invisible').classList.add('hide');
    document.getElementById('password-visible').classList.remove('hide');
});
// ----------------------------------------------
//    Upload User Profile Name, Bio, and Email
// ----------------------------------------------
const editProfileForm = document.forms.profile_edit;
const editPicture = editProfileForm.elements.profile_photo;
const editName = editProfileForm.elements.edit_name;
const editEmail = editProfileForm.elements.edit_email;
const editBio = editProfileForm.elements.edit_bio;
const updatedProfile = () => {
    showPage('user-profile');
}
document.getElementById('save-profile').addEventListener('click', () => {
    let body = {};
    if (editEmail.value !== '') {
        Object.assign(body, { email: editEmail.value });
    }
    if (editName.value !== '') {
        Object.assign(body, { name: editName.value });
    }
    if (editBio.value !== '') {
        Object.assign(body, { bio: editBio.value });
    }
    if (editPicture.files.length !== 0) {
        fileToDataUrl(editPicture.files[0]).then((e) => {
            Object.assign(body, { image: e });
            apiAuthPut('user', globalToken, body, updatedProfile);
        });
    } 
    apiAuthPut('user', globalToken, body, updatedProfile);
});
// Milestone 6
// ----------------------------------------------
//         Loading Messages Infinitely
// ----------------------------------------------
const infiniteloadMessage = () => {
    document.getElementById('loading-icon').classList.remove('hide');
    messageLoad(channelID, messageStartCount);
    document.getElementById('loading-icon').classList.add('hide');
}
const infiniteScroll = () => {
    const scrollMessages = document.querySelector('.message-container');

    scrollMessages.addEventListener('scroll', () => {
        if(scrollMessages.scrollTop === - scrollMessages.scrollHeight + scrollMessages.offsetHeight){
            messageStartCount = messageStartCount + 25;
            infiniteloadMessage(messageStartCount);
        }
    });
}
// ----------------------------------------------
//             Push Notifications
// ----------------------------------------------
document.getElementById('enable').addEventListener('click', (event) => {
    event.preventDefault();
    askNotificationPermission();
});
const askNotificationPermission = () => {
    const handlePermission = () => {
        document.getElementById('enable').style.display = (Notification.permission === "granted" ? "none" : "block");
    }
    if (!("Notification" in window)) {
        console.log("This browser does not support notifications.");
    } else {
        Notification.requestPermission().then((permission) => {
            handlePermission(permission);
        });
    }
}
const refresh = () => {
    if (Notification?.permission === "granted") {
        notifications.forEach((notif) => {
            if (notif.channelmembers.includes(userID) && globalToken !== null) {
                const n = new Notification(`You have received a new message!`, { body: notif.message === undefined ? notif.image : notif.message });
                notif.channelmembers.pop(userID);
            }
        });
    }
    setTimeout(refresh, 5000);
}

setTimeout(refresh, 1000);

// ----------------------------------------------
//          Cached Access to HomePage
// ----------------------------------------------

if (globalToken === null || userID === null) {
    showPage('login');
} else {
    showPage('channels');
}