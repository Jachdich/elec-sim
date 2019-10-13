# elec-sim

This is a simple FOSS logic simulator program. That's it.

## Running the source

### Using Eclipse

#### Linux
Download the zip and extract it into your eclipse workspace. Open eclipse and navigate to `src/main/java/com/cospox/elecsim/main.java` and click run.

#### Other platforms
Follow the linux instructions but before running there is one extra step. Navigate to `lib32` or `lib64` (depending on your architecture) and select the files ending in yout OS. Right-click them and choose `build path->add to build path`. Then click run and it should work.

### Using Java without Eclipse

#### Tutorial to come when I figure it out myself.

## Licence

wtf is a license anyway, pfft. Just use the code, I don't really care. Just don't sell it for money. Keep any changes free and open source.

See licence.txt.

### Processing

This program uses the Processing library (https://processing.org/) and I have included a copy of their licence at licence-processing.txt.

## Critical bugs

As this is a new (and alpha) project, there are a few recognised critical bugs in the codebase. Most of the bugs arise from the behaviour of ctrl-z. To completely remove the chance that these bugs happen then after ctrl-z'ing, save the file and reopen the program therefore it is impossible for the bugs to occur. I'm working to resolve the bugs however it is insanely complex.
