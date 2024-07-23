// Implementation of the Student DB ADT
// Written by HOLLY-CHEN (z5359932)
// at 10/03/2022

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "List.h"
#include "StudentDb.h"
#include "Tree.h"

struct studentDb {
    Tree byZid;
    Tree byName;
};

////////////////////////////////////////////////////////////////////////
// Comparison functions

/**
 * Compares two records by zid only and returns:
 * - A negative number if the first record is less than the second
 * - Zero if the records are equal
 * - A positive number if the first record is greater than the second
 */
int compareByZid(Record r1, Record r2) {
    return RecordGetZid(r1) - RecordGetZid(r2);
}

/**
 * Compares two records by name (family name first) and then by
 * zid if the names are equal, and returns:
 * - A negative number if the first record is less than the second
 * - Zero if the records are equal
 * - A positive number if the first record is greater than the second
 */
int compareByName(Record r1, Record r2) {
    int familyNamecmp = strcmp(RecordGetFamilyName(r1), RecordGetFamilyName(r2));
    int givenNamecmp = strcmp(RecordGetGivenName(r1), RecordGetGivenName(r2));
	
	// if family names are not the same, return the result of strcmp
	if (familyNamecmp != 0) {
		return familyNamecmp;
    // if the given names are not the same, return the result of strcmp
	} else if (givenNamecmp != 0) {
        return givenNamecmp;
    // names are the same, so compare zids
    } else {
        return compareByZid(r1, r2);
    }
}

////////////////////////////////////////////////////////////////////////

StudentDb DbNew(void) {
    StudentDb db = malloc(sizeof(*db));
    if (db == NULL) {
        fprintf(stderr, "error: out of memory\n");
        exit(EXIT_FAILURE);
    }

    db->byZid = TreeNew(compareByZid);
    db->byName = TreeNew(compareByName);
    return db;
}

void DbFree(StudentDb db) {
    TreeFree(db->byZid, false);
    TreeFree(db->byName, true);
    free(db);
}

////////////////////////////////////////////////////////////////////////

bool DbInsertRecord(StudentDb db, Record r) {
    if (TreeInsert(db->byZid, r)) {
        TreeInsert(db->byName, r);
        return true;
    } else {
        return false;
    }
}

////////////////////////////////////////////////////////////////////////

bool DbDeleteByZid(StudentDb db, int zid) {
    Record dummy = RecordNew(zid, "", "");
    Record r = TreeSearch(db->byZid, dummy);

    if (r != NULL) {
        TreeDelete(db->byZid, r);
        TreeDelete(db->byName, r);
        RecordFree(r);
        RecordFree(dummy);
        return true;
    } else {
        RecordFree(dummy);
        return false;
    }
}

////////////////////////////////////////////////////////////////////////

Record DbFindByZid(StudentDb db, int zid) {
    Record dummy = RecordNew(zid, "", "");
    Record r = TreeSearch(db->byZid, dummy);
    RecordFree(dummy);
    return r;
}

////////////////////////////////////////////////////////////////////////

List DbFindByName(StudentDb db, char *familyName, char *givenName) {
    // create dummy lower record and upper record
    Record new_lower_dummy = RecordNew(MIN_ZID, familyName, givenName);
    Record new_upper_dummy = RecordNew(MAX_ZID, familyName, givenName);
    // create new list which has all the records between lower and upper dummies.
    List new_list = TreeSearchBetween(db->byName, new_lower_dummy, new_upper_dummy);
    // free dummy records
    RecordFree(new_lower_dummy);
    RecordFree(new_upper_dummy);
    return new_list;
}

////////////////////////////////////////////////////////////////////////

void DbListByZid(StudentDb db) {
    TreeListInOrder(db->byZid);
}

void DbListByName(StudentDb db) {
    TreeListInOrder(db->byName);
}
