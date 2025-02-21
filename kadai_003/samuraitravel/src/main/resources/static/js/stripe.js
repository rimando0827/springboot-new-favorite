const stripe = Stripe('pk_test_51QmUIpQsYXMsFwaekoImTA4KzCLztcpilKbC3rJpZblQtAgFORoXRFzLERCXIlpkrEt95tbDVDCiJOn5i4GDivc900SnIgs75M');
const paymentButton = document.querySelector('#paymentButton');

paymentButton.addEventListener('click', () => {
 stripe.redirectToCheckout({
   sessionId: sessionId
 })
});