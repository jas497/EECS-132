#count em
data = open("US_Cities_Hypercube.txt", "r")
out = []
gen = []
for line in data.readlines():
	i = 0
	while i < len(line):
		if line[i] == ",":
			left = line[0:i]
			right= line[i+2:-1]
			if not (left in out):
				out.append(left)
			if not (right in out):
				out.append(right)
			gen.append(left)
			gen.append(right)
		i +=1
for each in out:
	qty = 0
	for every in gen:
		if each == every:
			qty +=1
	print(qty, each)
