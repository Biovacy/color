var getColorHTTP        = new XMLHttpRequest();
var submitColorHTTP     = new XMLHttpRequest();
var getPredictionHTTP   = new XMLHttpRequest();

getColorHTTP.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
       // Typical action to be performed when the document is ready:
       changeBackgroundColor(getColorHTTP.responseText);
    }
};

getPredictionHTTP.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
       // Typical action to be performed when the document is ready:
       console.log("Prediction: " + getPredictionHTTP.responseText);
    }
};

function colorRGBtoHEX(rgbcolor){
    // credit: https://stackoverflow.com/questions/49974145/how-to-convert-rgba-to-hex-color-code-using-javascript#49974627
    const rgba = rgbcolor.replace(/^rgba?\(|\s+|\)$/g, '').split(',');

    var hex = `${((1 << 24) + (parseInt(rgba[0]) << 16) + (parseInt(rgba[1]) << 8) + parseInt(rgba[2])).toString(16).slice(1)}`;

    return hex;
}

function getPrediction(){

    var request = "http://localhost:8080/api/evaluate?" + colorRGBtoHEX(getCurrentColor());

    getPredictionHTTP.open("GET", request, true);
    getPredictionHTTP.send();
}

function getCurrentColor() {
    var color = window.getComputedStyle(document.body, null).getPropertyValue('background-color');
    return color;
}

function newColor(){
    getColorHTTP.open("GET", "http://localhost:8080/api/getcolor", true);
    getColorHTTP.send();
    getPrediction();
};

function submitColor(like){
    var color = getCurrentColor();

    color = colorRGBtoHEX(color);

    var data = color + "&" + like;

    var apiCall = "http://localhost:8080/api/submitcolor?" + data;

    submitColorHTTP.open("GET", apiCall, true);
    submitColorHTTP.send();
};

function changeBackgroundColor(color){
    document.body.style.background = color;
};

document.getElementById("like").onclick = function() {
    submitColor(true);
    newColor();
};

document.getElementById("dislike").onclick = function() {
    submitColor(false);
    newColor();
};
