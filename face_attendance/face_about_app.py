import tkinter as tk
from tkinter import ttk
from tkinter import * 

root = tk.Tk()
root.title('Tentang Aplikasi')

# frame = Frame(root)
# frame.pack()

window_width = 600
window_height = 400

# dapatkan dimensi layar
screen_width = root.winfo_screenwidth()
screen_height = root.winfo_screenheight()

# temukan titik tengah
center_x = int(screen_width/2 - window_width / 2)
center_y = int(screen_height/2 - window_height / 2)

# atur posisi jendela ke tengah layar
root.geometry(f'{window_width}x{window_height}+{center_x}+{center_y}')

# bagian judul
labelJudul = tk.Label(root,
        borderwidth = 2,
        width = 600,
        wraplength=500,
        relief="ridge",
        font=("Helvetica", 12),
        text="Identifikasi Wajah Untuk Absensi Menggunakan Metode KNN"
)

# bagian identitas pengembang
labelNama = tk.Label(root,
        text="Ibnu Ramadhan A | 1911501961",
        font=("Helvetica", 12)
)

# bagian detail informasi program aplikasi
labelDetail = tk.Label(root,
        font=("Helvetica", 11),
        wraplength=600,
        text="Program aplikasi ini digunakan untuk mengambil data wajah karyawan yang nantinya dapat mengenali wajah seseorang untuk keperluan absensi menggunakan metode KNN (Metode K-Nearest Neighbors) dan menggunakan Android dan Tinter untuk antarmukanya.",
        anchor='w'
)

labelDetail2 = tk.Label(root,
        font=("Helvetica", 11),
        wraplength=600,
        text="Mekanisme identifikasi wajah menggunakan aplikasi ini adalah sebagai berikut:",
        anchor='w'
)

labelDetail3 = tk.Label(root,
        font=("Helvetica", 11),
        wraplength=600,
        text="1. Proses pengambilan data wajah dan preprocessing, 2. Pelatihan (training), pengujian (testing), dan penghitungan akurasi, presisi, dan recall, 3. Identifikasi wajah dan penampilan informasi nama, 4. Menjalankan API untuk absensi pada perangkat Android.",
        anchor='w'
)

# memanggil label variabel dengan fungsi Pack()
labelJudul.pack(ipadx=5, ipady=5, pady=5)
labelNama.pack()
labelDetail.pack(fill='both', pady=20)
labelDetail2.pack(fill='both')
labelDetail3.pack(fill='both')

root.mainloop()