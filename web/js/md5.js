function md5(file) {
    var md5 = "";
    var fileReader = new FileReader(),
        box = document.getElementById('box');
    blobSlice = File.prototype.mozSlice || File.prototype.webkitSlice || File.prototype.slice,
        // file = document.getElementById("file").files[0],
        chunkSize = 2097152,
        // read in chunks of 2MB
        chunks = Math.ceil(file.size / chunkSize),
        currentChunk = 0,
        spark = new SparkMD5();

    fileReader.onload =  function (e) {
        spark.appendBinary(e.target.result); // append binary string
        currentChunk++;

        if (currentChunk < chunks) {
            loadNext();
        }
        else {
            app.uplodmd5(file, spark.end());
        }
    };

    function loadNext() {
        var start = currentChunk * chunkSize,
            end = start + chunkSize >= file.size ? file.size : start + chunkSize;

        fileReader.readAsBinaryString(blobSlice.call(file, start, end));
    };

     loadNext();
     
    return md5;
}