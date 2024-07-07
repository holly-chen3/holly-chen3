/**
 * Given a js file object representing a jpg or png image, such as one taken
 * from a html file input element, return a promise which resolves to the file
 * data as a data url.
 * More info:
 *   https://developer.mozilla.org/en-US/docs/Web/API/File
 *   https://developer.mozilla.org/en-US/docs/Web/API/FileReader
 *   https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/Data_URIs
 * 
 * Example Usage:
 *   const file = document.querySelector('input[type="file"]').files[0];
 *   console.log(fileToDataUrl(file));
 * @param {File} file The file to be read.
 * @return {Promise<string>} Promise which resolves to the file as a data url.
 */
export function fileToDataUrl(file) {
    const validFileTypes = [ 'image/jpeg', 'image/png', 'image/jpg' ]
    const valid = validFileTypes.find(type => type === file.type);
    // Bad data, let's walk away.
    if (!valid) {
        throw Error('provided file is not a png, jpg or jpeg image.');
    }
    
    const reader = new FileReader();
    const dataUrlPromise = new Promise((resolve,reject) => {
        reader.onerror = reject;
        reader.onload = () => resolve(reader.result);
    });
    reader.readAsDataURL(file);
    return dataUrlPromise;
}

// Error Message Popup
const alertPlaceholder = document.getElementById('liveAlertPlaceholder');
export const appendAlert = (message) => {
    const alertElement = document.getElementById('alert-template').cloneNode(true);
    alertElement.removeAttribute('id');
    alertElement.classList.add('temporary-alert');
    alertElement.classList.remove('hide');
    alertElement.querySelector('.alert-message').innerText = message;
    alertPlaceholder.append(alertElement);
};

// POST function
export const apiPost = (path, body, callback) => {
    fetch('http://localhost:5005/' + path, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(body)
    })
    .then ((response) => {
        return response.json();
    })
    .then ((data) => {
        if (data.error) {
            appendAlert(data.error, 'warning');
        } else {
            callback(data);
        }
    });
};

export const apiAuthPost = (path, token, body, callback) => {
    fetch('http://localhost:5005/' + path, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(body)
    })
    .then ((response) => {
        return response.json();
    })
    .then ((data) => {
        console.log(data);
        if (data.error) {
            appendAlert(data.error);
        } else {
            callback(data);
        }
    });
};

// GET function
export const apiAuthGet = (path, token, callback, errorCallback) => {
    fetch('http://localhost:5005/' + path, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
    })
    .then((response) => response.json())
    .then((data) => {
        if (data.error) {
            appendAlert(data.error);
            errorCallback();
        } else {
            callback(data);
        }
    });
};

export const apiAuthQueryGet = (path, query, token, callback, errorCallback) => {
    fetch('http://localhost:5005/' + path + '?' + query, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
    })
    .then((response) => response.json())
    .then((data) => {
        if (data.error) {
            appendAlert(data.error);
            errorCallback();
        } else {
            callback(data);
        }
    });
};

export const apiAuthPut = (path, token, body, callback) => {
    fetch('http://localhost:5005/' + path, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(body)
    })
    .then ((response) => {
        return response.json();
    })
    .then ((data) => {
        console.log(data);
        if (data.error) {
            appendAlert(data.error);
        } else {
            callback(data);
        }
    });
};

export const apiAuthDelete = (path, token, callback) => {
    fetch('http://localhost:5005/' + path, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    })
    .then ((response) => {
        return response.json();
    })
    .then ((data) => {
        console.log(data);
        if (data.error) {
            appendAlert(data.error);
        } else {
            callback(data);
        }
    });
};