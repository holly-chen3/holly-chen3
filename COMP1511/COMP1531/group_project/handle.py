'''
Simple program to simulate how handles are generated.
'''

handles = []


def handle_is_not_unique(handle):
    '''
    Return true if the given handle is not unique
    Return false otherwise
    '''
    handle_not_unique = False
    for item in handles:
        if item == handle:
            handle_not_unique = True

    return handle_not_unique

def generate_handle(first, last):
    """
    Generate a new, unique handle.
    """
    handle = (first + last)[:20]
    # FIXME
    # add code here
    handle = ''.join(filter(str.isalnum, handle))
    num = 0
    while handle_is_not_unique(handle): # johnsmith0
        # add code
        #num is 1
        switch = True
        while switch and num > 0:
            handle = handle[:-1] #johnsmith
            switch = False

        handle = handle + str(num) #johnsmith1
        num += 1

    return handle


def main():
    '''
    No need to change this.
    '''
    i = 0
    while True:
        i += 1
        name_first = input(f"{i} - Enter name_first: ")
        name_last = input(f"{i} - Enter name_last:  ")
        new_handle = generate_handle(name_first, name_last)
        handles.append(new_handle)

        print(f"{i} - New handle is {new_handle}")
        print(f"{i} - All handles - {handles}")
        print("ctrl+d to quit\n")


if __name__ == '__main__':
    try:
        main()
    except (EOFError, KeyboardInterrupt):
        raise SystemExit(f"Exiting due to ctrl+d or ctrl+c.")