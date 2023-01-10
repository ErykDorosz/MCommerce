const express = require("express");
const app = express();
const { resolve } = require("path");
const stripe = require("stripe")("sk_test_51MK2khF5Dy2GOkWXqQic0UaHIYP7BAh4doGKSHuXJTHkW5ynXybx8PpwxMX68KgWGBJZI3C7HmPMZbdkDJCKoplr00pTCLNsyE");
app.use(express.static("."));
app.use(express.json());

const calculateOrderAmount = items => {
  console.log(items[0].amount)
  return items[0].amount;
};
// app.post("/create-payment-intent", async (req, res) => {
//   const { items } = req.body;
//   const { currency } = req.body;

//   // Create a PaymentIntent with the order amount and currency
//   const paymentIntent = await stripe.paymentIntents.create({
//     amount: calculateOrderAmount(items),
//     currency: currency
//   });
//   res.send({
//     clientSecret: paymentIntent.client_secret
//   });
// });

const PORT = 5001;
app.listen(PORT, () => console.log('Node server listening on port ${PORT}'));

app.post('/payment-sheet', async (req, res) => {
  const { items } = req.body;
  const { currency } = req.body;
  const customer = await stripe.customers.create();
  const ephemeralKey = await stripe.ephemeralKeys.create(
    {customer: customer.id},
    {apiVersion: '2022-11-15'}
  );
  
  const paymentIntent = await stripe.paymentIntents.create({
    amount: calculateOrderAmount(items)*100,
    currency: 'pln',
    customer: customer.id,
    automatic_payment_methods: {
      enabled: true,
    },
  });

  res.json({
    paymentIntent: paymentIntent.client_secret,
    ephemeralKey: ephemeralKey.secret,
    customer: customer.id,
    publishableKey: 'pk_test_51MK2khF5Dy2GOkWXwmTOX2wEMxbnumVCNsOgYI3zpiMZQUWOXRHqOgGfQ66OAoetpAABCLmbsn3dlOkRkMR7xoA400R502C6kC'
  });
});