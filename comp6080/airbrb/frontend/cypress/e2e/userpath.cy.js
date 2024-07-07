describe('user advanced happy path', () => {
  it('should register and create listings successfully', () => {
    window.window.cy.visit('http://localhost:3000/');
    window.cy.url().should('include', 'localhost:3000');
    window.cy.get('[name="icon-button-open"]')
      .click(); // Open the user menu
    window.cy.get('[name="Register"]')
      .click(); // Click the "Register" button
    window.cy.url().should('include', 'localhost:3000/Register');
    window.cy.get('input[name="Email"]')
      .focus()
      .type('chen@gmail.com');
    window.cy.get('input[name="Name"]')
      .focus()
      .type('Holly');
    window.cy.get('input[name="Password"]')
      .focus()
      .type('chen');
    window.cy.get('input[name="Confirm Password"]')
      .focus()
      .type('chen');
    window.cy.get('button[name="Register"]')
      .click();
    window.cy.url().should('include', 'localhost:3000/HostedListings');
    window.cy.get('button[name="new-listing-button"]')
      .click();
    window.cy.url().should('include', 'localhost:3000/CreateListings');
    window.cy.get('input[name="title"]')
      .focus()
      .type('Two Storey House in Bondi Junction');
    window.cy.get('input[name="street"]')
      .focus()
      .type('3 Henry Street');
    window.cy.get('input[name="city"]')
      .focus()
      .type('Bondi Junction');
    window.cy.get('input[name="state"]')
      .focus()
      .type('NSW');
    window.cy.get('input[name="postcode"]')
      .focus()
      .type('2233');
    window.cy.get('input[name="country"]')
      .focus()
      .type('Australia');
    window.cy.get('input[name="price"]')
      .focus()
      .type('200');
    window.cy.get('input[name="thumbnail"]')
      .selectFile('src/assets/Flower_Wallpaper_Desktop_3_1.jpeg');
    window.cy.get('input[name="propertyType"]')
      .focus()
      .type('House');
    window.cy.get('input[name="numBaths"]')
      .focus()
      .type('3');
    window.cy.get('input[name="numBedrooms"]')
      .focus()
      .type('4');
    window.cy.get('input[name="beds"]')
      .focus()
      .type('6');
    window.cy.get('input[name="amenities"]')
      .focus()
      .type('Toilet, Wifi, Spa, Gym');
    window.cy.get('[name="create-listing-button"]')
      .click();
    window.cy.url().should('include', 'localhost:3000/HostedListings');
    window.cy.get('button[name="new-listing-button"]')
      .click();
    window.cy.url().should('include', 'localhost:3000/CreateListings');
    window.cy.get('input[name="title"]')
      .focus()
      .type('Three Storey Beach House in Manly');
    window.cy.get('input[name="street"]')
      .focus()
      .type('5 William Street');
    window.cy.get('input[name="city"]')
      .focus()
      .type('Manly');
    window.cy.get('input[name="state"]')
      .focus()
      .type('NSW');
    window.cy.get('input[name="postcode"]')
      .focus()
      .type('2255');
    window.cy.get('input[name="country"]')
      .focus()
      .type('Australia');
    window.cy.get('input[name="price"]')
      .focus()
      .type('300');
    window.cy.get('input[name="thumbnail"]')
      .selectFile('src/assets/Flower_Wallpaper_Desktop_6_1.jpeg');
    window.cy.get('input[name="propertyType"]')
      .focus()
      .type('House');
    window.cy.get('input[name="numBaths"]')
      .focus()
      .type('5');
    window.cy.get('input[name="numBedrooms"]')
      .focus()
      .type('5');
    window.cy.get('input[name="beds"]')
      .focus()
      .type('7');
    window.cy.get('input[name="amenities"]')
      .focus()
      .type('Wifi, Spa, Gym, Bar');
    window.cy.get('[name="create-listing-button"]')
      .click();
    window.cy.url().should('include', 'localhost:3000/HostedListings');
  });
  it('should publish two listings and logout successfully', () => {
    window.cy.get('[name="publish-button"]:eq(0)')
      .click();
    window.cy.get('[name="publish-start-date"]')
      .focus()
      .type('2023-11-18');
    window.cy.get('[name="publish-end-date"]')
      .focus()
      .type('2023-11-30');
    window.cy.get('[name="publish-listing"]')
      .click();
    window.cy.get('[name="close-publish-modal"]')
      .click();
    window.cy.get('[name="publish-button"]:eq(1)')
      .click();
    window.cy.get('[name="publish-start-date"]')
      .focus()
      .type('2023-11-19');
    window.cy.get('[name="publish-end-date"]')
      .focus()
      .type('2023-11-29');
    window.cy.get('[name="publish-listing"]')
      .click();
    window.cy.get('[name="close-publish-modal"]')
      .click();
    window.cy.url().should('include', 'localhost:3000/HostedListings');
    window.cy.get('[name="auth-icon-button"]')
      .click(); // Open the user menu
    window.cy.get('[name="Logout"]')
      .click(); // Click the "Logout" button
    window.cy.url().should('include', 'localhost:3000/');
  });
  it('should register second user and book a property successfully', () => {
    window.cy.get('[name="icon-button-open"]')
      .click(); // Open the user menu
    window.cy.get('[name="Register"]')
      .click(); // Click the "Register" button
    window.cy.url().should('include', 'localhost:3000/Register');
    window.cy.get('input[name="Email"]')
      .focus()
      .type('wen@gmail.com');
    window.cy.get('input[name="Name"]')
      .focus()
      .type('Janet');
    window.cy.get('input[name="Password"]')
      .focus()
      .type('wen');
    window.cy.get('input[name="Confirm Password"]')
      .focus()
      .type('wen');
    window.cy.get('button[name="Register"]')
      .click();
    window.cy.url().should('include', 'localhost:3000/HostedListings');
    window.cy.get('[name="Home"]')
      .filter(':visible')
      .click();
    window.cy.url().should('include', 'localhost:3000/');
    window.cy.get('[name="view-listing"]:eq(0)')
      .click();
    window.cy.get('[name="booking-start-date"]')
      .focus()
      .type('2023-11-18')
    window.cy.get('[name="booking-end-date"]')
      .focus()
      .type('2023-11-20')
    window.cy.get('[name="booking-button"]')
      .click()
    window.cy.wait(1000)
    window.cy.get('[name="booking-dates"]')
      .invoke('text')
      .should('eq', 'Start Date: 2023-11-18 End Date: 2023-11-20')
    window.cy.get('[name="auth-icon-button"]')
      .click();
    window.cy.get('[name="Logout"]')
      .click();
  });
  it('should log back in the first user and edit a hosted listing', () => {
    window.cy.wait(1000);
    window.cy.get('[name="icon-button-open"]')
      .click();
    window.cy.get('[name="Login"]')
      .click();
    window.cy.get('input[name="login-email"]')
      .focus()
      .type('chen@gmail.com');
    window.cy.get('input[name="login-password"]')
      .focus()
      .type('chen');
    window.cy.get('button[name="login"]')
      .click();
    window.cy.url().should('include', 'localhost:3000/HostedListings');
    window.cy.get('[name="edit-button"]:eq(0)')
      .click();
    window.cy.url().should('include', 'localhost:3000/EditListings');
    // Add property images
    window.cy.get('input[name="images"]')
      .selectFile('src/assets/molang-desktop-nirvana.jpeg');
    window.cy.wait(2000);
    window.cy.get('input[name="images"]')
      .selectFile('src/assets/molang-desktop-velvetunderground.jpeg');
    window.cy.wait(2000);
    window.cy.get('[name="save-changes"]')
      .click();
    window.cy.wait(2000);
    window.cy.url().should('include', 'localhost:3000/HostedListings');
    window.cy.get('[name="Home"]')
      .filter(':visible')
      .click();
    window.cy.wait(2000);
    window.cy.get('[name="view-listing"]:eq(0)')
      .click();
    // see if property images is displayed
    window.cy.wait(4000);
    window.cy.get('[name="image-carousel"]:eq(1)')
      .scrollIntoView()
      .should('be.visible');
  });
  it('should let host accept booking', () => {
    window.cy.get('[name="Hosted Listings"]')
      .filter(':visible')
      .click();
    window.cy.get('[name="bookings-button"]:eq(0)')
      .click();
    window.cy.get('[name="accept-booking"]:eq(0)')
      .click();
    window.cy.get('[name="booking-request-alert"]')
      .invoke('text')
      .should('eq', 'Booking was successfully accepted');
  });
  it('should login to second user to let user leave review', () => {
    window.cy.get('[name="auth-icon-button"]')
      .click();
    window.cy.get('[name="Logout"]')
      .click();
    window.cy.wait(1000);
    window.cy.get('[name="icon-button-open"]')
      .click();
    window.cy.get('[name="Login"]')
      .click();
    window.cy.get('input[name="login-email"]')
      .focus()
      .type('wen@gmail.com');
    window.cy.get('input[name="login-password"]')
      .focus()
      .type('wen');
    window.cy.get('button[name="login"]')
      .click();
    window.cy.get('[name="Home"]')
      .filter(':visible')
      .click();
    window.cy.get('[name="view-listing"]:eq(0)')
      .click();
    window.cy.wait(1000);
    window.cy.get('[name="text-review"]')
      .focus()
      .type('It was really good!');
    window.cy.get('[name="rating"]')
      .focus()
      .type('5');
    window.cy.get('[name="submit-review"]')
      .click();
    window.cy.get('[name="user-review"]:eq(0)')
      .invoke('text')
      .should('eq', 'wen@gmail.comRating: 5/5Review: It was really good!');
  });
  it('should let user search filter the ratings of listings', () => {
    window.cy.get('[name="Home"]')
      .filter(':visible')
      .click();
    window.cy.get('[name="select-rating"]')
      .focus()
      .type('ascending');
    window.cy.get('[name="card-title"]')
      .invoke('text')
      .should('eq', 'Three Storey Beach House in ManlyTwo Storey House in Bondi Junction');
  });
});
