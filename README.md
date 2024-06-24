# Marvel Search Engine Application
A desktop GUI Java application that utilises Marvel and Imgur API for users to search Marvel characters and comic information, and save a screenshot of the main window to the desktop.

Input API: Marvel

Output API: Imgur

## Feature
1. The user is asked for a number from 3 to 50 when the application starts. This number is a 'popularity threshold'. If a character that is searched has more than the  threshold of comics, the application should pop up an alert saying 'This character is popular!'

2. Allow the user to save a screenshot of the main window scene to disk.
    
3. Theme song. The application should play a music track on a loop. The user should have the ability to 
    turn this feature on and off.
    
4. Implement a Spinning Progress Indicator during API calls.

## Citation
https://stackoverflow.com/questions/415953/how-can-i-generate-an-md5-hash-in-java#:~:text=Call%20MessageDigest.,in%20one%20operation%20with%20md

    byte[] bytesOfMessage = yourString.getBytes("UTF-8");
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] theMD5digest = md.digest(bytesOfMessage);

https://www.callicoder.com/generate-qr-code-in-java-using-zxing/

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, QR_CODE_IMAGE_WIDTH, QR_CODE_IMAGE_HEIGHT);
        Path path = FileSystems.getDefault().getPath(QR_CODE_IMAGE_PATH);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

https://stackoverflow.com/questions/36492084/how-to-convert-an-image-to-base64-string-in-java

    File f = new File(QR_CODE_IMAGE_PATH);
    FileInputStream fileInputStream = new FileInputStream(f);
    byte[] bytes = new byte[(int) f.length()];
    fileInputStream.read(bytes);
    fileInputStream.close();
    String encodedFile = Base64.getEncoder().encodeToString(bytes);

https://apidocs.imgur.com/#c85c9dfc-7487-4de2-9ecd-66f727cf3139

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("image",imageBase64)
                .build();
        Request request = new Request.Builder()
                .url("https://api.imgur.com/3/image")
                .method("POST", body)
                .addHeader("Authorization", "Client-ID "+key)
                .build();
        Response response = client.newCall(request).execute();

The background music of the application is Melancholy written by White Cherry, achieved from https://www.youtube.com/watch?v=nejMJbXj2pU

https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/file-chooser.htm#CCHICECF

    MenuItem cmItem2 = new MenuItem("Save Image");
    cmItem2.setOnAction((ActionEvent e) -> {
        FileChooser fileChooser1 = new FileChooser();
        fileChooser1.setTitle("Save Image");
        System.out.println(pic.getId());
        File file = fileChooser1.showSaveDialog(stage);
        if (file != null) {
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(pic.getImage(),
                    null), "png", file);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    });

