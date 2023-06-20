from flask import Flask, jsonify, request
import pickle
import face_recognition
import time
from io import BytesIO
import base64

# Initialize the Flask application
app = Flask(__name__)

# These are the extension that we are accepting to be uploaded
app.config['ALLOWED_EXTENSIONS'] = set(['jpg','jpeg','png'])

# For a given file, return whether it's an allowed type or not
def allowed_file(filename):
    return '.' in filename and \
           filename.rsplit('.', 1)[1].lower() in app.config['ALLOWED_EXTENSIONS']

## This function you need to modify as per your need
## Here verifying if face value coming from mobile device is 
## matched with our dataset, send that person name back to mobile
## so you can show success message, like - "XYZ attandance"
@app.route('/api/facedetect', methods=['GET','POST'])
def upload_image():
    if request.method == "GET":
        resp = jsonify({"message":"Error", "data'":"Method not allowed"})
        resp.status_code = 405
        return resp
    else:
        # check if the post request has the files part
        requestData = request.form
        if 'face' not in requestData:
            resp = jsonify({"message":"Error", "data'":"No face found"})
            resp.status_code = 500
            return resp
        # Get the list of the uploaded files
        file = BytesIO(base64.decodebytes(requestData["face"].encode()))
        start = time.time()
        results = []
        with open("model/trained_knn_model.clf", 'rb') as f:
            knn_clf = pickle.load(f)
            
            image = face_recognition.load_image_file(file)
            face_location = face_recognition.face_locations(image)
            
            if len(face_location) != 0:
                # Find encodings for faces in the test iamge
                faces_encodings = face_recognition.face_encodings(image, known_face_locations=face_location)

                # Use the KNN model to find the best matches for the test face
                closest_distances = knn_clf.kneighbors(faces_encodings, n_neighbors=9)
                are_matches = [closest_distances[0][i][0] <= 0.4 for i in range(len(face_location))]
                predictions = [(pred, loc) if rec else ("unknown", loc) for pred, loc, rec in zip(knn_clf.predict(faces_encodings), face_location, are_matches)]
                lp = 0
                for name, (top, right, bottom, left) in predictions:
                    resarray = {}
                    resarray["name"] = name
                    resarray["accuracy"] = closest_distances[0][lp][0]
                    results.append(resarray)
                    lp = lp + 1

        print(results)
        if(results is None):
            resp = jsonify({"message":"Error", "data'":"No face found"})
            resp.status_code = 500
            return resp
        else:
            ## Here based on yoour requirements you can save data.
            ## Like person ID and date of attendance. 
            ## It will execute all the time , so manage flag if attendence already done.
            ## This Part Read the face, and if face match , it will send the person name with accuracy value to
            ## android app. 
            resp = jsonify({"message":"success", "data": results})
            resp.status_code = 200
            return resp

if __name__ == '__main__':
    app.run(port=5000, host="0.0.0.0", debug=True, threaded=True, processes=1)