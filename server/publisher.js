const amqp = require('amqplib/callback_api')
const connect = require('./connect').connect

const publish = async (exname, message, durable = false) => {
    /** Adiciona uma fila e envia uma mensagem */
    message = message.toString()
    let [conn, channel] = await connect(process.env.BROKER_ADDRESS || 'localhost')

    channel.assertExchange(exname, 'fanout', { durable: durable })
    channel.publish(exname, '', Buffer.from(message))
    console.log(' - Sent to ' + exname)

    setTimeout(function () { conn.close() }, 100);
}
module.exports = publish
