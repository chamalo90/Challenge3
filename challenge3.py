mport hashlib
import struct

def to_byte(input):
  # I = Integer = 4 bytes
  return struct.pack('I', input)
   
def sha256_3(input):
  m1 = hashlib.sha256(to_byte(input))
  m2 = hashlib.sha256(m1.digest())
  m3 = hashlib.sha256(m2.digest())
  return m3.hexdigest()

if __name__ == "__main__":
  A = 0x12345678
  if bin(A).count("1")%2 != 1:
    print "hamming weight is even"
  B = sha256_3(A)
  print B
