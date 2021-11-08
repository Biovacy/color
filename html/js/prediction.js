/* INIT */

document.getElementById("R").value = 0;
document.getElementById("G").value = 0;
document.getElementById("B").value = 0;

var getPredictionHTTP = new XMLHttpRequest();

/* FUNCTIONS */

getPredictionHTTP.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
       // Typical action to be performed when the document is ready:
       document.getElementById("display_result").textContent = getPredictionHTTP.responseText;
    }
};

function changeBackgroundColor(color){
    document.body.style.background = "#" + color;
};

function colorRGBtoHEX(rgbcolor){
    // credit: https://stackoverflow.com/questions/49974145/how-to-convert-rgba-to-hex-color-code-using-javascript#49974627
    const rgba = rgbcolor.replace(/^rgba?\(|\s+|\)$/g, '').split(',');

    var hex = `${((1 << 24) + (parseInt(rgba[0]) << 16) + (parseInt(rgba[1]) << 8) + parseInt(rgba[2])).toString(16).slice(1)}`;

    return hex;
}

function getColor(){
    var R = document.getElementById("R").value;
    var G = document.getElementById("G").value;
    var B = document.getElementById("B").value;

    if (R == "") { R = 0; }
    if (G == "") { G = 0; }
    if (B == "") { B = 0; }

    var rgb = "rgb(" + R + ", " + G + ", " + B + ")"

    changeBackgroundColor(colorRGBtoHEX(rgb));
}

function checkInputs() {
    var R = document.getElementById("R").value;
    var G = document.getElementById("G").value;
    var B = document.getElementById("B").value;

    if (R > 255) { document.getElementById("R").value = 255; }
    if (G > 255) { document.getElementById("G").value = 255; }
    if (B > 255) { document.getElementById("B").value = 255; }

    if (R < 0) { document.getElementById("R").value = 0; }
    if (G < 0) { document.getElementById("G").value = 0; }
    if (B < 0) { document.getElementById("B").value = 0; }
}

function displayColor() {
    checkInputs()
    getColor()
}

function getCurrentColor() {
    var color = window.getComputedStyle(document.body, null).getPropertyValue('background-color');
    return color;
}

function buttonClick(){
    var request = "http://localhost:8080/api/evaluate?" + colorRGBtoHEX(getCurrentColor());

    getPredictionHTTP.open("GET", request, true);
    getPredictionHTTP.send();
}

/* START FUNCTIONS */

buttonClick();
