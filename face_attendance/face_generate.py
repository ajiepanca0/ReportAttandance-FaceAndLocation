import tkinter as tk
import os
import cv2
import imutils
import dlib
import mysql.connector
import hashlib

# Inisialisasi detector wajah
detector = dlib.get_frontal_face_detector()

# Inisialisasi jendela Tkinter
window = tk.Tk()
window.title('Augmentation and Preprocessing Page')
window.geometry('500x150')

# Variabel status folder
folder_created = False

# Fungsi untuk membuat folder dan menjalankan skrip augmentasi dan preprocessing
def create_folder_and_run_script():
    global folder_created
    folder_name = entry.get()
    if folder_name:
        folder_path = "dataset/" + folder_name
        if not os.path.exists(folder_path):
            os.makedirs(folder_path)
            # Jalankan skrip mengambil gambar di sini
            capture_images(folder_path)
            resize_images(folder_path)  # Resize gambar
            label_status.config(text="Folder '{}' berhasil dibuat dan gambar diambil.".format(folder_name))
            # Set folder_created ke True
            folder_created = True
        else:
            label_status.config(text="Folder '{}' sudah ada.".format(folder_name))
    else:
        label_status.config(text="Masukkan nama folder.")

def insert_data():
    nip = nip_entry.get()
    first_name = first_name_entry.get()
    last_name = last_name_entry.get()
    email = email_entry.get()
    phone = phone_entry.get()
    password = password_entry.get()
    

    if nip and first_name and last_name and email and phone and password:

        password_hash = hashlib.md5(password.encode()).hexdigest()


        # Terkoneksi ke database MySQL
        conn = mysql.connector.connect(
            host='localhost',
            user='root',
            password='',
            database='report'
        )

        cursor = conn.cursor()

        # Insert data ke tabel
        insert_query = "INSERT INTO pegawai (nip, first_name, last_name, email, phone, password) VALUES (%s, %s, %s, %s, %s, %s)"
        values = (nip, first_name, last_name, email, phone, password_hash)
        cursor.execute(insert_query, values)

        # Commit perubahan dan tutup koneksi
        conn.commit()
        conn.close()

        clear_form()
        status_label.config(text="Data berhasil dimasukkan ke MySQL.")
    else:
        status_label.config(text="Mohon lengkapi semua field.")

def clear_form():
    nip_entry.delete(0, tk.END)
    first_name_entry.delete(0, tk.END)
    last_name_entry.delete(0, tk.END)
    email_entry.delete(0, tk.END)
    phone_entry.delete(0, tk.END)
    password_entry.delete(0, tk.END)

# Fungsi untuk mengambil gambar dari webcam
def capture_images(folder_path):
    camera = cv2.VideoCapture(0)
    count = 0
    gray_count = 0

    while count < 50:  # Ambil 50 gambar
        _, frame = camera.read()
        frame = imutils.resize(frame, width=500)

        # Ubah frame menjadi grayscale
        gray_frame = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

        rects = detector(gray_frame, 0)  # Deteksi wajah

        if len(rects) > 0:
            rect = rects[0]
            (x, y, w, h) = (rect.left(), rect.top(), rect.width(), rect.height())

            # Tampilkan kotak pada wajah yang terdeteksi
            cv2.rectangle(frame, (x, y), (x + w, y + h), (0, 255, 0), 2)

            if gray_count < 25:
                # Ambil wajah dalam bentuk grayscale
                face = gray_frame[y:y + h, x:x + w]
                gray_count += 1
            else:
                # Ambil wajah dari frame asli (berwarna)
                face = frame[y:y + h, x:x + w]

            # Simpan gambar wajah
            image_path = os.path.join(folder_path, f"{count}.jpg")
            cv2.imwrite(image_path, face)

            count += 1

        cv2.imshow("Capture Images", frame)
        if cv2.waitKey(1) & 0xFF == ord('q'):
            # Hapus folder yang telah dibuat jika tombol 'q' ditekan sebelum folder_created menjadi True
            global folder_created
            if not folder_created:
                os.rmdir(folder_path)
            label_status.config(text="Folder tidak jadi dibuat.")
            break

    camera.release()
    cv2.destroyAllWindows()

# Fungsi untuk meresize gambar menjadi 108x108
def resize_images(folder_path):
    for filename in os.listdir(folder_path):
        if filename.endswith(".jpg"):
            image_path = os.path.join(folder_path, filename)
            image = cv2.imread(image_path)
            resized_image = cv2.resize(image, (108, 108))
            cv2.imwrite(image_path, resized_image)

# Label dan input untuk nama folder
label_folder = tk.Label(window, text="Masukkan nama folder:")
label_folder.grid(row=0, column=0, padx=5, pady=5)

entry = tk.Entry(window)
entry.grid(row=0, column=1, padx=5, pady=5)

# Tombol submit
button_submit = tk.Button(window, text="Submit", command=create_folder_and_run_script)
button_submit.grid(row=0, column=2, padx=5, pady=5)

# Label status
label_status = tk.Label(window, text="")
label_status.grid(row=1, column=0, columnspan=3, padx=5, pady=5)

# Label dan input untuk data diri
nip_label = tk.Label(window, text="NIP:")
nip_label.grid(row=2, column=0, padx=5, pady=5)
nip_entry = tk.Entry(window)
nip_entry.grid(row=2, column=1, padx=5, pady=5)

first_name_label = tk.Label(window, text="Nama Depan:")
first_name_label.grid(row=3, column=0, padx=5, pady=5)
first_name_entry = tk.Entry(window)
first_name_entry.grid(row=3, column=1, padx=5, pady=5)

last_name_label = tk.Label(window, text="Nama Belakang:")
last_name_label.grid(row=4, column=0, padx=5, pady=5)
last_name_entry = tk.Entry(window)
last_name_entry.grid(row=4, column=1, padx=5, pady=5)

email_label = tk.Label(window, text="Email:")
email_label.grid(row=5, column=0, padx=5, pady=5)
email_entry = tk.Entry(window)
email_entry.grid(row=5, column=1, padx=5, pady=5)

phone_label = tk.Label(window, text="Nomor Telepon:")
phone_label.grid(row=6, column=0, padx=5, pady=5)
phone_entry = tk.Entry(window)
phone_entry.grid(row=6, column=1, padx=5, pady=5)

password_label = tk.Label(window, text="Password:")
password_label.grid(row=7, column=0, padx=5, pady=5)
password_entry = tk.Entry(window, show="*")
password_entry.grid(row=7, column=1, padx=5, pady=5)

# Tombol insert data
button_insert = tk.Button(window, text="Insert Data", command=insert_data)
button_insert.grid(row=8, column=0, padx=5, pady=5)

# Label status data
status_label = tk.Label(window, text="")
status_label.grid(row=8, column=1, padx=5, pady=5)

# Jalankan aplikasi
window.mainloop()
