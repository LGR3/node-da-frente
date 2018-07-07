var fs = require('fs');
var fastCsv = require('fast-csv');
var fileStream = fs.createReadStream("dados.csv"),
parser = fastCsv();
var utm = require('utm');
fileStream
.on("readable", function () {
    var data;
    while ((data = fileStream.read()) !== null) {
        parser.write(data);
    }
})
.on("end", function () {
    parser.end();
});

parser
.on("readable", function () {
    var data;
    while ((data = parser.read()) !== null) {
        const row = data[0].split(';');
        if(row[6] > 0 && row[7] > 0){
            const obj = utm.toLatLon(row[6], row[7], 25, 'l');
            row[6] = obj.latitude;
            row[7] = obj.longitude;
        }
        console.log(row.join(';'));
        fs.appendFile('csv.txt',row.join(';')+'\n', () => {
        });

    }
})
.on("end", function () {
    console.log(carros);
    // fs.appendFile('carros.txt',']', () => {
    //     // console.log('merda')
    // });
});