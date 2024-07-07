// POST function
export const ApiPost = (path, body, callback, errorCallback) => {
  fetch('http://localhost:5005/' + path, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(body)
  })
    .then((response) => {
      return response.json();
    })
    .then((data) => {
      if (data.error) {
        errorCallback(data.error);
      } else {
        callback(data);
      }
    });
}
export const ApiAuthPost = (path, token, body, callback, errorCallback) => {
  fetch('http://localhost:5005/' + path, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`
    },
    body: JSON.stringify(body)
  })
    .then((response) => {
      return response.json();
    })
    .then((data) => {
      if (data.error) {
        console.log(data.error);
        errorCallback(data.error);
      } else {
        callback(data);
      }
    });
}
// GET function
export const ApiGet = (path, callback, errorCallback) => {
  fetch('http://localhost:5005/' + path, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    }
  })
    .then((response) => {
      return response.json();
    })
    .then((data) => {
      if (data.error) {
        errorCallback(data.error);
      } else {
        callback(data);
      }
    });
}
export const ApiAuthGet = async (path) => {
  console.log(localStorage.getItem('token'));
  try {
    const response = await fetch('http://localhost:5005/' + path, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${localStorage.getItem('token')}`
      },
    });
    const data = await response.json();
    return data;
  } catch (error) {
    return await error;
  }
}
// DELETE function
export const ApiDelete = (path) => {
  fetch('http://localhost:5005/' + path, {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${localStorage.getItem('token')}`
    }
  })
    .then((response) => {
      return response.json();
    })
    .then((data) => {
      if (data.error) {
        alert(data.error);
      } else {
        return data;
      }
    });
}
// PUT function
export const ApiPut = (path, data) => {
  return new Promise((resolve, reject) => {
    fetch('http://localhost:5005/' + path, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${localStorage.getItem('token')}`
      },
      body: JSON.stringify(data)
    })
      .then((res) => res.json())
      .then((resData) => {
        if (resData.error) {
          reject(resData.error);
        } else {
          resolve(resData);
        }
      })
      .catch((error) => {
        reject(error);
      });
  })
}
