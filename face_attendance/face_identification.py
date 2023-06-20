import pickle
import face_recognition
import cv2
import threading

# Muat model KNN yang telah dilatih
with open("model/trained_knn_model.clf", 'rb') as f:
    knn_clf = pickle.load(f)

# Inisialisasi objek penangkapan video
video_capture = cv2.VideoCapture(0)

# Fungsi untuk memulai penangkapan video
def start_video_stream():
    while True:
        # Baca video frame per frame
        ret, frame = video_capture.read()

        # Ubah frame dari BGR ke RGB
        rgb_frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)

        # Temukan lokasi wajah dalam frame
        X_face_locations = face_recognition.face_locations(rgb_frame)

        if len(X_face_locations) != 0:
            # Temukan enkoding wajah dalam frame
            faces_encodings = face_recognition.face_encodings(rgb_frame, known_face_locations=X_face_locations, model="large")

            # Gunakan model KNN untuk mencari kemiripan terbaik untuk wajah uji
            closest_distances = knn_clf.kneighbors(faces_encodings, n_neighbors=9)
            are_matches = [closest_distances[0][i][0] <= 0.4 for i in range(len(X_face_locations))]
            predictions = [(pred, loc) if rec else ("unknown", loc) for pred, loc, rec in
                           zip(knn_clf.predict(faces_encodings), X_face_locations, are_matches)]

            for name, (top, right, bottom, left) in predictions:
                # Gambar persegi panjang di sekitar wajah
                cv2.rectangle(frame, (left, top), (right, bottom), (0, 0, 255), 2)

                # Gambar label dengan nama di bawah wajah
                cv2.rectangle(frame, (left, bottom - 35), (right, bottom), (0, 0, 255), cv2.FILLED)
                font = cv2.FONT_HERSHEY_DUPLEX
                cv2.putText(frame, name, (left + 6, bottom - 6), font, 0.7, (255, 255, 255), 1)

                # Hitung akurasi
                num_matches = sum(are_matches)
                total_faces = len(X_face_locations)
                accuracy = num_matches / total_faces

                # Tambahkan informasi akurasi dalam bentuk frame
                accuracy_text = f"Akurasi: {accuracy:.2f}"
                cv2.putText(frame, accuracy_text, (10, 30), cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0, 0, 255), 2)

        # Tampilkan frame yang dihasilkan
        cv2.imshow('Video', frame)

        # Hentikan loop ketika tombol 'q' ditekan
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

# Mulai penangkapan video dalam thread terpisah
video_thread = threading.Thread(target=start_video_stream)
video_thread.start()

# Tunggu thread penangkapan video selesai
video_thread.join()

# Setelah thread penangkapan video selesai, lepaskan sumber daya
video_capture.release()
cv2.destroyAllWindows()