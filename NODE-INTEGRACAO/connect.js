const amqp = require('amqplib/callback_api')

const connect = async (address) => {
    /** Conecta com o broker do endereço informado e retorna a conexao e o canal */
    let conn = await new Promise((res, rej) =>
        amqp.connect('amqp://' + address, (err, conn) => {
            if (err) { rej(err) }
            else { res(conn) }
        })
    )
    let ch = await new Promise((res, rej) =>
        conn.createChannel((err, channel) => {
            if (err) { rej(err) }
            else { res(channel) }
        })
    )
    return [conn, ch]
}
exports.connect = connect