const publish = require('./publisher')
const subscriber = require('./subscriber')

publish('bus', 3333) //cliente pede onibus 3333

subscriber('3333', (msg) => {
    console.log('- Receive:', msg)
})
