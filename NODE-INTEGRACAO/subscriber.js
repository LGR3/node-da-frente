const amqp = require('amqplib/callback_api')
const connect = require('./connect').connect


const subscribe = async (exname, callback, durable = false, noAck = true) => {
    /** Se inscreve em uma exchange fanout */
    let [conn, channel] = await connect(process.env.BROKER_ADDRESS || 'localhost')

    channel.assertExchange(exname, 'fanout', { durable: durable })

    let q = await new Promise((res, rej) =>
        channel.assertQueue('', { exclusive: false }, (err, queue) => {
            if (err) { rej() }
            else { res(queue) }
        })
    )
    channel.bindQueue(q.queue, exname, '')

    console.log(' - Waiting messages for ' + exname)
    channel.consume(q.queue, (msg) => {
        let message = msg.content.toString()
        // console.log(`[${exname}]: ${message}`)
        callback(message)

    }, { noAck: noAck }) //diz se precisa de ack para garantir que não vai perder a mensagem por indisponibilidade de quem a pegou

}
exports.subscribe = subscribe

const subscribeQueue = async (queuename, callback, noAck = false) => {
    /** Se inscreve em uma queue */
    let [conn, channel] = await connect(process.env.BROKER_ADDRESS || 'localhost')


    channel.assertQueue(queuename, { durable: false })
    
    channel.consume(queuename, (msg) => {
        let message = msg.content.toString()
        callback(message)
    }, { noAck: noAck })

}
exports.subscribeQueue = subscribeQueue
