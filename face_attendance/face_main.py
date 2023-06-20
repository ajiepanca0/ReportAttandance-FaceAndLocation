from tkinter import filedialog
from tkinter import Canvas
from tkinter import ttk
import tkinter as tk
import cv2
import numpy as np
import webbrowser
import shutil
import glob
import os

# frame
root = tk.Tk(className=' Identifikasi Wajah Untuk Absensi Menggunakan Metode KNN ')

window_width = 700
window_height = 500

huruf=('Times',12,'italic')

# mendapatkan dimensi layar
screen_width = root.winfo_screenwidth()
screen_height = root.winfo_screenheight()

# menemukan titik tengah / posisi tengah
center_x = int(screen_width/2 - window_width / 2)
center_y = int(screen_height/2 - window_height / 2)

# mengatur posisi jendela di tengah layar
root.geometry(f'{window_width}x{window_height}+{center_x}+{center_y}')

# label = judul skripsi
labelJudul = tk.Label(root,
        borderwidth = 2,
        width = 600,
        wraplength=600,
        relief="ridge",
        font=("Helvetica", 12),
        text="IDENTIFIKASI WAJAH UNTUK ABSENSI")

labelJudul.pack(ipadx=5, ipady=5, pady=5)

# menampilkan label gambar
photo = tk.PhotoImage(file='./assets/img/face_recognition.png')
image_label = ttk.Label(
    root,
    justify="center",
    image=photo,
    padding=5
)
image_label.pack()

# -----------------------fungsi-fungsi | tombol--------------------------------
# tombol "About The App"
def btn_info_app():
    os.system("face_about_app.py")

# tombol "Generate and Preprocessing"
def btn_generate():
    os.system("python face_generate.py")
    
# tombol "Training, Testing, and Calculation Akurasi-Presisi-Recall"
def btn_training_knn():
    os.system("python face_training_testing.py")

# tombol "Identify Images Testing"
def btn_identification():
    os.system("python face_identification.py")

# tombol "Jalankan Api"
def btn_api():
    os.system("python face_api.py")

# tombol "Jalankan import Excel"
def btn_excel():
    os.system("python import_data.py")


# tombol-tombol kontrol
button1 = ttk.Button(root, text='About The App', command=btn_info_app, width=100)
button1.pack(padx=5, pady=5, ipady=3)

button2 = ttk.Button(root, text='Generate and Preprocessing', command=btn_generate, width=100)
button2.pack(padx=5, pady=5, ipady=3)

button4 = ttk.Button(root, text='Training, Testing, and Calculation Akurasi-Presisi-Recall', command=btn_training_knn, width=100)
button4.pack(padx=5, pady=5, ipady=3)

button5 = ttk.Button(root, text='Identify Images Testing', command=btn_identification, width=100)
button5.pack(padx=5, pady=5, ipady=3)

button6 = ttk.Button(root, text='Running Api', command=btn_api, width=100)
button6.pack(padx=5, pady=5, ipady=3)

button7 = ttk.Button(root, text='Import Excel', command=btn_excel, width=100)
button7.pack(padx=5, pady=5, ipady=3)

# loop utama
root.mainloop()
