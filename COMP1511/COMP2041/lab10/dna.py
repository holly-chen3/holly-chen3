import re
from collections import Counter
def read_dna(dna_file):
    """
    Read a DNA string from a file.
    the file contains data in the following format:
    A <-> T
    G <-> C
    G <-> C
    C <-> G
    G <-> C
    T <-> A
    Output a list of touples:
    [
        ('A', 'T'),
        ('G', 'C'),
        ('G', 'C'),
        ('C', 'G'),
        ('G', 'C'),
        ('T', 'A'),
    ]
    Where either (or both) elements in the string might be missing:
    <-> T
    G <->
    G <-> C
    <->
    <-> C
    T <-> A
    Output:
    [
        ('', 'T'),
        ('G', ''),
        ('G', 'C'),
        ('', ''),
        ('', 'C'),
        ('T', 'A'),
    ]
    """
    dnaList = []
    with open(dna_file, 'r') as outFile:
    	for line in outFile.readlines():
    		line = re.sub('\s', '', line)
    		pairs = tuple(re.split('<->', line))
    		dnaList.append(pairs)
    return dnaList

def is_rna(dna):
    """
    Given DNA in the aforementioned format,
    return the string "DNA" if the data is DNA,
    return the string "RNA" if the data is RNA,
    return the string "Invalid" if the data is neither DNA nor RNA.
    DNA consists of the following bases:
    Adenine  ('A'),
    Thymine  ('T'),
    Guanine  ('G'),
    Cytosine ('C'),
    RNA consists of the following bases:
    Adenine  ('A'),
    Uracil   ('U'),
    Guanine  ('G'),
    Cytosine ('C'),
    The data is DNA if at least 90% of the bases are one of the DNA bases.
    The data is RNA if at least 90% of the bases are one of the RNA bases.
    The data is invalid if more than 10% of the bases are not one of the DNA or RNA bases.
    Empty bases should be ignored.
    """
    dictDNA = {
    	'A': 0,
    	'T': 0,
    	'G': 0,
    	'C': 0,
    	'U': 0
    }
    for pair in dna:
    	firstPair = pair[0]
    	secondPair = pair[1]
    	if firstPair != "":
    		dictDNA[firstPair] += 1
    	if secondPair != "":
    		dictDNA[secondPair] += 1
    count = 0
    for val in dictDNA.values():
    	count += val
    DNA = (count - dictDNA['U'])/count
    RNA = (count - dictDNA['T'])/count
    if DNA >= 0.9:
    	return 'DNA'
    elif RNA >= 0.9:
    	return 'RNA'
    else:
    	return 'Invalid'
    
def clean_dna(dna):
    """
    Given DNA in the aforementioned format,
    If the pair is incomplete, ('A', '') or ('', 'G'), ect
    Fill in the missing base with the match base.
    In DNA 'A' matches with 'T', 'G' matches with 'C'
    In RNA 'A' matches with 'U', 'G' matches with 'C'
    If a pair contains an invalid base the pair should be removed.
    Pairs of empty bases should be ignored.
    """
    typeDNA = is_rna(dna)
    matchDNA = {
    	'A': 'T' if is_rna(dna) == "DNA" else 'U',
    	'T': 'A',
    	'C': 'G',
    	'G': 'C',
    	'U': 'A'
    }
    matchedDNA = []
    for pair in dna:
    	pair = list(pair)
    	if pair[0] == '' and pair[1] == '':
    		continue
    	if pair[0] == '':
    		pair[0] = matchDNA[pair[1]]
    	elif pair[1] == '':
    		pair[1] = matchDNA[pair[0]]
    	matchedDNA.append(tuple(pair))
    return matchedDNA

def mast_common_base(dna):
    """
    Given DNA in the aforementioned format,
    return the most common first base:
    eg. given:
    A <-> T
    G <-> C
    G <-> C
    C <-> G
    G <-> C
    T <-> A
    The most common first base is 'G'.
    Empty bases should be ignored.
    """
    countBases = Counter([pair[0] for pair in dna])
    return countBases.most_common(1)[0][0]

def base_to_name(base):
    """
    Given a base, return the name of the base.
    The base names are:
    Adenine  ('A'),
    Thymine  ('T'),
    Guanine  ('G'),
    Cytosine ('C'),
    Uracil   ('U'),
    return the string "Unknown" if the base isn't one of the above.
    """
    bases = {
    	'A': 'Adenine',
    	'T': 'Thymine', 
    	'G': 'Guanine',
    	'C': 'Cytosine',
    	'U': 'Uracil'
    }
    if base in 'ATGCU':
    	return bases[base]
    else:
    	return 'Unknown'
    
    
    
