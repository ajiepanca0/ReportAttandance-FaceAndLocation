from tkinter import messagebox
from sklearn import neighbors
from sklearn.model_selection import train_test_split
import os
import os.path
import pickle
import face_recognition
import numpy as np
from sklearn.metrics import confusion_matrix, precision_score, recall_score, accuracy_score, classification_report
import seaborn as sns
import matplotlib.pyplot as plt

ALLOWED_EXTENSIONS = {'png', 'jpg', 'jpeg'}

# Data latih berisi semua enkode wajah dari semua gambar yang dikenal, dan labelnya adalah nama mereka
def train(train_dir, model_save_path=None, n_neighbors=9, knn_algo='ball_tree', verbose=False):
    encodings = []
    names = []

    # Direktori Pelatihan
    train_people = os.listdir(train_dir)
    print(train_people)

    # Perulangan akan melalui setiap dataset dalam direktori pelatihan
    for person in train_people:
        pix = os.listdir(os.path.join(train_dir, person))

        # Loop melalui setiap gambar latih untuk orang yang saat ini
        for person_img in pix:
            # Dapatkan enkode wajah untuk wajah di setiap file gambar
            print(os.path.join(train_dir, person, person_img))
            face = face_recognition.load_image_file(os.path.join(train_dir, person, person_img))
            print(face.shape)

            # Anggap seluruh gambar adalah lokasi wajah
            height, width, _ = face.shape
            face_location = (0, width, height, 0)
            face_enc = face_recognition.face_encodings(face, known_face_locations=[face_location])
            face_enc = np.array(face_enc)
            face_enc = face_enc.flatten()

            # Tambahkan enkode wajah untuk gambar saat ini dengan label yang sesuai (nama) ke data latihan
            encodings.append(face_enc)
            names.append(person)

    # print(np.array(encodings).shape)

    # Bagi data menjadi set latihan dan pengujian
    unique_names = np.unique(names)
    encodings_train = []
    encodings_test = []
    names_train = []
    names_test = []
    for name in unique_names:
        name_encodings = [encoding for encoding, n in zip(encodings, names) if n == name]
        name_labels = [n for n in names if n == name]
        encodings_train_name, encodings_test_name, names_train_name, names_test_name = train_test_split(
            name_encodings, name_labels, test_size=0.3, random_state=42)
        encodings_train.extend(encodings_train_name)
        encodings_test.extend(encodings_test_name)
        names_train.extend(names_train_name)
        names_test.extend(names_test_name)

    # Buat dan latih klasifikasi KNN
    knn_clf = neighbors.KNeighborsClassifier(n_neighbors=n_neighbors, algorithm=knn_algo, weights='distance')
    knn_clf.fit(encodings_train, names_train)

    # Evaluasi klasifikasi pada data pengujian
    predictions = knn_clf.predict(encodings_test)
    accuracy = accuracy_score(names_test, predictions)
    precision = precision_score(names_test, predictions, average='weighted')
    recall = recall_score(names_test, predictions, average='weighted')
    report = classification_report(names_test, predictions)

    # # Tambahkan kode untuk menampilkan jarak inklusi
    # distances, indices = knn_clf.kneighbors(encodings_test)

    # for i, distance in enumerate(distances):
    #     print(f"Data testing ke-{i + 1}:")
    #     for j, dist in enumerate(distance):
    #         training_data_index = indices[i][j]
    #         training_data_name = names_train[training_data_index]
    #         testing_data_name = names_test[i]  # Menyimpan nama data testing
    #         print(f"Jarak inklusi dengan data training {training_data_name} dan data testing {testing_data_name}: {dist}")

    # Cetak hasil Akurasi, Presisi, Recall, dan Laporan Klasifikasi
    print("Akurasi:", accuracy)
    print("Presisi:", precision)
    print("Recall:", recall)
    print("Laporan Klasifikasi:\n", report)

    # Simpan klasifikasi KNN yang telah dilatih
    if model_save_path is not None:
        with open(model_save_path, 'wb') as f:
            pickle.dump(knn_clf, f)

    num_training_data = len(encodings_train)
    num_testing_data = len(encodings_test)

    messagebox.showinfo("Pelatihan Selesai",
                        f"Pelatihan selesai!\n\nAkurasi: {accuracy:.2f}\nPresisi: {precision:.2f}\nRecall: {recall:.2f}\n\nJumlah data latihan: {num_training_data}\nJumlah data pengujian: {num_testing_data}")

    # Buat confusion matrix
    cm = confusion_matrix(names_test, predictions)
    class_names = np.unique(names)
    plot_confusion_matrix(cm, class_names)
    plt.savefig('confusion_Matrix.png')

    return knn_clf


def plot_confusion_matrix(cm, classes):
    plt.figure(figsize=(8, 6))
    sns.heatmap(cm, annot=True, fmt='d', cmap='Blues', xticklabels=classes, yticklabels=classes)
    plt.title('Confusion Matrix')
    plt.xlabel('Predicted Label')
    plt.ylabel('True Label')
    plt.xticks(rotation=45)


if __name__ == "__main__":
    # Latih klasifikasi KNN dan simpan ke disk
    print("Mengtraining klasifikasi KNN...")
    classifier = train("dataset", model_save_path="model/trained_knn_model.clf", n_neighbors=9)
    print("Pelatihan selesai!")
