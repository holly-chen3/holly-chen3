console.log('Hello world - view me in the Console of developer tools');

const formItems = document.getElementsByTagName("form");
const textItems = document.querySelectorAll('input[type="text"]');
const streetName = document.getElementById("street-name");
const suburb = document.getElementById("suburb");
const postCode = document.getElementById("postcode");
const dateOfBirth = document.getElementById("dob");
const buildingType = document.getElementById("building-type");
const checkBoxes = document.getElementsByName("features");
const heating = document.getElementById("features-heating");
const airConditioning = document.getElementById("features-airconditioning");
const pool = document.getElementById("features-pool");
const sandpit = document.getElementById("features-sandpit");
let selectAll = document.getElementById("select-all-btn");
const reset = document.getElementById("reset-form");
let formResult = document.getElementById("form-result");

const regexPostcode = new RegExp(/^\d{4}$/);
const regexDob = new RegExp(/[0-9]{2}\/[0-9]{2}\/[0-9]{4}$/);

const render = () => {
    let dobVal = dateOfBirth.value;
    const dobArray = dobVal.split('/');
    const dob = dobArray[1] + '/' + dobArray[0] + '/' + dobArray[2];
    if (streetName.value.length < 3 || streetName.value.length > 50) {
        return "Please input a valid street name";
    } else if (suburb.value.length < 3 || suburb.value.length > 50) {
        return "Please input a valid suburb";
    } else if (!regexPostcode.test(postCode.value)) {
        return "Please input a valid postcode";
    } else if (!regexDob.test(dobVal) || isNaN(new Date(dob))) {
        return "Please enter a valid date of birth";
    } else {
        return `You are ${calculateAge(dob)} years old, and your address is ${streetName.value} St, ${suburb.value}, ${postCode.value}, Australia. Your building is ${buildingType.value === "apartment" ? "an" : "a"} ${buildingType.value}, and it has ${featurePrint(checkBoxes)}`;
    }
};

Array.from(textItems).forEach(element => {
    element.addEventListener('blur', () => {
        formResult.value = render();
    });
});

buildingType.addEventListener('change', () => {
    formResult.value = render();
});

for (let i = 0; i < checkBoxes.length; i++) {
    checkBoxes[i].addEventListener( "change", () => {
        selectAll.value = "Deselect all";
        for ( let i = 0; i < checkBoxes.length; i++ ) {
            if (checkBoxes[i].checked === false) {
                selectAll.value = "Select All";
            }
        }
        formResult.value = render();
    });
}

selectAll.addEventListener('click', () => {
    if (selectAll.value === "Deselect all") {
        for (let i = 0; i < checkBoxes.length; i++) {
            checkBoxes[i].checked = false;
        }
        selectAll.value = "Select All";
    } else {
        for (let i = 0; i < checkBoxes.length; i++) {
            checkBoxes[i].checked = true;
        }
        selectAll.value = "Deselect all";
    }
    formResult.value = render();
});

reset.addEventListener('click', () => {
    for (let i = 0; i < formItems.length; i++) {
        formItems[i].reset();
    }
});

const calculateAge = (birth) => {
    let difference = new Date(Date.now() - new Date(birth).getTime());
    return Math.abs(difference.getUTCFullYear() - 1970);
};

const featurePrint = (features) => {
    let checkedArray = [];
    let str = "no features";
    for (let i = 0; i < features.length; i++) {
        if (features[i].checked) {
            checkedArray.push(features[i].value);
        }
    }
    
    for (let i = 0; i < checkedArray.length; i++) {
        if (i === checkedArray.length - 1) {
            str = (str === "no features" ? '' : str + ', and ') + checkedArray[i];
        } else {
            str = (str === "no features" ? '' : str + ', ') + checkedArray[i];
        }
    }
    return str;
};