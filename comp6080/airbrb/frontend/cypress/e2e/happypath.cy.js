describe('user happy path', () => {
  it('should navigate to Landing Page successfully', () => {
    window.cy.visit('http://localhost:3000/');
    window.cy.url().should('include', 'localhost:3000');
  })
  it('should navigate to Register Screen successfully', () => {
    window.cy.get('[name="icon-button-open"]')
      .click(); // Open the user menu
    window.cy.get('[name="Register"]')
      .click(); // Click the "Register" button
    window.cy.url().should('include', 'localhost:3000/Register');
  })
  it('should register successfully', () => {
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
  })
  it('should navigate to create new listing screen successfully', () => {
    window.cy.get('button[name="new-listing-button"]')
      .click();
    window.cy.url().should('include', 'localhost:3000/CreateListings');
  })
  it('should create new listing screen successfully', () => {
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
  })
  it('should update the thumbnail and title of the listing successfully', () => {
    window.cy.get('[name="card-thumbnail"]')
      .should('be.visible');
    window.cy.get('[name="card-title"]')
      .should('be.visible');
    window.cy.get('[name="card-title"]')
      .invoke('text')
      .should('eq', 'Two Storey House in Bondi Junction');
    window.cy.url().should('include', 'localhost:3000/HostedListings');
  })
  it('should publish a listing successfully', () => {
    window.cy.get('[name="publish-button"]')
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
    window.cy.url().should('include', 'localhost:3000/HostedListings');
  })
  it('should logout successfully', () => {
    window.cy.get('[name="auth-icon-button"]')
      .click(); // Open the user menu
    window.cy.get('[name="Logout"]')
      .click(); // Click the "Logout" button
    window.cy.url().should('include', 'localhost:3000/');
  })
  it('should register another user successfully', () => {
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
  })
  it('should make a booking successfully', () => {
    window.cy.get('[name="Home"]')
      .filter(':visible')
      .click();
    window.cy.url().should('include', 'localhost:3000/');
    window.cy.get('[name="view-listing"]')
      .click();
    window.cy.get('[name="booking-start-date"]')
      .focus()
      .type('2023-11-18')
    window.cy.get('[name="booking-end-date"]')
      .focus()
      .type('2023-11-20')
    window.cy.get('[name="booking-button"]')
      .click()
    window.cy.get('[name="booking-dates"]')
      .invoke('text')
      .should('eq', 'Start Date: 2023-11-18 End Date: 2023-11-20')
  })
  it('should navigate to Login Screen successfully', () => {
    window.cy.get('[name="auth-icon-button"]')
      .click(); // Open the user menu
    window.cy.get('[name="Logout"]')
      .click(); // Click the "Logout" button
    window.cy.url().should('include', 'localhost:3000/');
    window.cy.get('[name="icon-button-open"]')
      .click(); // Open the user menu
    window.cy.get('[name="Login"]')
      .click(); // Click the "Login" button
    window.cy.url().should('include', 'localhost:3000/Login');
  })
  it('should login successfully', () => {
    window.cy.get('input[name="login-email"]')
      .focus()
      .type('chen@gmail.com');
    window.cy.get('input[name="login-password"]')
      .focus()
      .type('chen');
    window.cy.get('button[name="login"]')
      .click();
    window.cy.url().should('include', 'localhost:3000/HostedListings');
  })
  it('should unpublish a listing successfully', () => {
    window.cy.get('[name="publish-button"]')
      .invoke('text')
      .should('eq', 'Unpublish');
    window.cy.get('[name="publish-button"]')
      .click();
    window.cy.get('[name="publish-button"]')
      .invoke('text')
      .should('eq', 'Publish');
    window.cy.url().should('include', 'localhost:3000/HostedListings');
  })
})
