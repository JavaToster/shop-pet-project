function sendFile() {
  const fileInput = document.getElementById('image');
  const file = fileInput.files[0];

  const reader = new FileReader();
  reader.onload = function(event) {
    const arrayBuffer = event.target.result;
    const byteArray = new Uint8Array(arrayBuffer);
    const base64String = btoa(String.fromCharCode.apply(null, byteArray));

    const jsonData = {
      fileName: file.name,
      fileContent: base64String
    };

    const xhr = new XMLHttpRequest();
    xhr.open('POST', 'http://192.168.1.4:8080/upload-item', true);
    xhr.setRequestHeader('Content-Type', 'application/json');

    xhr.onload = function() {
      if (xhr.status === 200) {
        console.log('File uploaded successfully');
      } else {
        console.log('File upload failed');
      }
    };

    xhr.send(JSON.stringify(jsonData));
  };

  reader.readAsArrayBuffer(file);

//    const fileInput = document.getElementById('image');
//    const file = fileInput.files[0];
//
//    const formData = new FormData();
//    formData.append('file', file);
//
//    fetch('/upload',{
//        method: 'POST',
//        body: formData
//    })
//    .then(response => response.json())
//    .then(data => console.log(data))
//    .catch(error => console.error(error));
}