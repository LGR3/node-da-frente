const publish = require('./publisher')
const subscribe = require('./subscriber').subscribe
const subscribeQueue = require('./subscriber').subscribeQueue

publish('bus', 3333) //call 3333, after getAllBus return [ '3333' ]

subscribe('3333', (msg) => { 
    //return example: msg = '3333;E1;3333;2018-01-22 13:33:56.740;1;1;289257;9113824;NULL;NULL;NULL;NULL;NULL'
    console.log('- Receive:', msg)
})


/** getting all bus names */
let allBus = {}
subscribeQueue('allbus', (msg) => {
    allBus[msg] = ''
    
})
const getAllBus = () => {
    return Object.keys(allBus)
}