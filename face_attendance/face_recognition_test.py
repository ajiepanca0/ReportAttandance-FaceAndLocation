import PIL.Image
import dlib
import numpy as np
from PIL import ImageFile

ImageFile.LOAD_TRUNCATED_IMAGES = True

face_encoder = dlib.face_recognition_model_v1("model/dlib_face_recognition_resnet_model_v1.dat")
face_detector = dlib.get_frontal_face_detector()
pose_predictor_68_point = dlib.shape_predictor("model/shape_predictor_68_face_landmarks.dat")
pose_predictor_5_point = dlib.shape_predictor("model/shape_predictor_5_face_landmarks.dat")

def _raw_face_locations(face_image, number_of_times_to_upsample=2, model="hog"):
    return face_detector(face_image, number_of_times_to_upsample)

def _raw_face_landmarks(face_image, face_locations=None, model="large"):
    face_locations = _raw_face_locations(face_image)
    if model == "small":
        pose_predictor = pose_predictor_5_point
    else:
        pose_predictor = pose_predictor_68_point
    return [pose_predictor(face_image, face_location) for face_location in face_locations]

def face_encodings(face_image, known_face_locations=None, num_jitters=2, model="large"):
    raw_landmarks = _raw_face_landmarks(face_image)
    return [np.array(face_encoder.compute_face_descriptor(face_image, raw_landmark_set, num_jitters)) for raw_landmark_set in raw_landmarks]

def load_image_file(file, mode='RGB'):
    im = PIL.Image.open(file)
    if mode:
        im = im.convert(mode)
    return np.array(im)
